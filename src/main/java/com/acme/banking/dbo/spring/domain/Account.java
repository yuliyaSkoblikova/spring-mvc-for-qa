package com.acme.banking.dbo.spring.domain;

import com.fasterxml.jackson.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import javax.validation.constraints.*;

@ApiModel(subTypes = {SavingAccount.class, CheckingAccount.class})
@Entity //TODO JPA Entity semantics
@Inheritance @DiscriminatorColumn(name="ACCOUNT_TYPE")
@JsonPropertyOrder({ "id", "type", "email", "amount" }) //TODO Jackson annotations semantics: https://www.baeldung.com/jackson-annotations & https://github.com/FasterXML/jackson-annotations
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = SavingAccount.class, name = "S"),
    @JsonSubTypes.Type(value = CheckingAccount.class, name = "C")
})
public abstract class Account {
    /** TODO Validation Framework */
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @PositiveOrZero private long id;
    @Email @Size(max = 50) private String email;
    private double amount;

    /** No-arg constructor needed by JPA and Jackson */
    public Account() { }

    //TODO Or Jackson can use this constructor: https://stackoverflow.com/questions/39123030/jackson-json-deserialization-with-multiple-parameters-constructor
    public Account(double amount, String email) {
        this.amount = amount;
        this.email = email;
    }

    public long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public double getAmount() {
        return amount;
    }

    @ApiModelProperty(allowableValues = "S,C")
    @JsonIgnore
    public abstract String getType();

    /** TODO Mutable state */
    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Account)) return false;

        Account account = (Account) o;

        if (Double.compare(account.getAmount(), getAmount()) != 0) return false;
        return getEmail().equals(account.getEmail());
    }

    @Override
    public int hashCode() {
        int result = 0;
        long temp;
        result = 31 * result + getEmail().hashCode();
        temp = Double.doubleToLongBits(getAmount());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return getType() + " " + getEmail() + " " + getAmount();
    }
}

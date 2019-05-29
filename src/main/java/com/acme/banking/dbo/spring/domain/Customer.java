package com.acme.banking.dbo.spring.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

public class Customer {

    @Size(max = 50) private String name;
    @GeneratedValue(strategy = GenerationType.IDENTITY) @PositiveOrZero private long id;


    @JsonCreator
    public Customer( long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        int result = 0;
        result = 31 * result + getName().hashCode();
        return result;
    }
}

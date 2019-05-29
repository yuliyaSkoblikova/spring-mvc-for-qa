package com.acme.banking.dbo.spring.configuration;

import com.acme.banking.dbo.spring.domain.CheckingAccount;
import com.acme.banking.dbo.spring.domain.SavingAccount;
import com.fasterxml.classmate.TypeResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

@Configuration
@EnableSwagger2
@Profile("!integrationTest")
public class SwaggerConfig {
    @Autowired private TypeResolver typeResolver;

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.acme.banking.dbo.spring.controller"))
                .paths(PathSelectors.any())
                .build()
                    .apiInfo(apiInfo())
                .additionalModels(typeResolver.resolve(CheckingAccount.class))
                .additionalModels(typeResolver.resolve(SavingAccount.class));
    }

    private ApiInfo apiInfo() {
        return new ApiInfo(
                "My REST API",
                "Some custom description of API.",
                "API TOS",
                "Terms of service",
                new Contact("Eugene Krivosheyev", "www.acme.com", "ekr@bk.ru"),
                "License of API", "API license URL", Collections.emptyList());
    }
}

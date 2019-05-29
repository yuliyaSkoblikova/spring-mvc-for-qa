package com.acme.banking.dbo.spring;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

/**
 * How to Run:
 * > java -jar target/dbo-1.0-SNAPSHOT.jar -Dfile.encoding=Cp866
 * or
 * > mvn clean spring-boot:run
 */
@SpringBootApplication
@ImportResource("classpath:spring-config.xml")
public class Application {
    public static void main(String... args) {
        SpringApplication.run(Application.class, args);
    }

    /** For customisation and reuse of all RestTemplates within application */
    //TODO Semantics of prototype
    @Bean
    public RestTemplate getRestTemplate(RestTemplateBuilder builder) {
        return builder
                .setConnectTimeout(Duration.ofSeconds(1))
                .setReadTimeout(Duration.ofSeconds(1))
            .build();
    }

    /** For customisation and reuse of all ObjectMappers within application */
    //TODO Semantics of singleton
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Bean
    public ObjectMapper getObjectMapper() {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
        return objectMapper;
    }
}
package com.acme.banking.dbo.spring.it;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK) //TODO Semantics of MOCK: No servlet container started
@AutoConfigureMockMvc
//TODO @JsonTest https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#boot-features-testing-spring-boot-applications-testing-autoconfigured-json-tests
public class AccountControllerLightST {
    @Autowired private MockMvc mockMvc;
    @Autowired Logger logger;

    @Test
    public void shouldGetPrePopulatedAccountsFromFakeDb() throws Exception {
        logger.info("Got test accounts: " +
            mockMvc.perform(get("/api/accounts").header("X-API-VERSION", "1"))
                    .andExpect(status().is2xxSuccessful())
                    .andExpect(jsonPath("$.length()").value("4"))
                    .andReturn()
                        .getResponse().getContentAsString()
        );
    }

    @Test
    public void shouldGetFirstPrePopulatedAccountFromFakeDb() throws Exception {
        mockMvc.perform(get("/api/accounts/1").header("X-API-VERSION", "1"))
                .andExpect(status().isFound())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.type").value("S"))
                .andExpect(jsonPath("$.email").value("a@a.ru"))
                .andExpect(jsonPath("$.amount").value("100.0"));
    }
}

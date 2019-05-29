package com.acme.banking.dbo.spring.it;


import com.acme.banking.dbo.spring.dao.AccountRepository;
import com.acme.banking.dbo.spring.domain.SavingAccount;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
//TODO @SpringBootTest VS @WebMvcTest(MyController.class): focus only on the web layer and not start a complete ApplicationContext
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK) //TODO Semantics of MOCK: No servlet container started
@AutoConfigureMockMvc
public class AccountControllerIT {
    @Autowired private MockMvc mockMvc; //TODO Exception handling issue: https://github.com/spring-projects/spring-boot/issues/7321#issuecomment-261343803
    @Autowired private Logger logger;
    @MockBean private AccountRepository accounts; //TODO MockBean semantics
    //TODO If not @SpringBootTest use @TestExecutionListeners(MockitoTestExecutionListener.class)
    //TODO @SpyBean semantics

    @Test
    public void shouldGetNoAccountsWhenAccountsRepoIsEmpty() throws Exception {
        when(accounts.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/accounts").header("X-API-VERSION", "1"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.length()").value("0"));
    }

    @Test
    public void shouldGetTheOneAccountWhenRepoIsPrePopulated() throws Exception {
        SavingAccount accountStub = new SavingAccount(100., "sa@sa.sa");
        when(accounts.findAll()).thenReturn(asList(accountStub));

        mockMvc.perform(get("/api/accounts").header("X-API-VERSION", "1"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.length()").value("1"))
                    .andExpect(jsonPath("$[0].amount").value("100.0"))
                    .andExpect(jsonPath("$[0].email").value("sa@sa.sa"));
    }
}

package com.acme.banking.dbo.spring.it;

import com.acme.banking.dbo.spring.domain.Account;
import com.acme.banking.dbo.spring.domain.CheckingAccount;
import com.acme.banking.dbo.spring.domain.SavingAccount;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.MultiValueMap;

import java.net.URI;
import java.net.URISyntaxException;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT) //TODO @LocalServerPort if RANDOM_PORT
@AutoConfigureMockMvc
@TestPropertySource("classpath:application.properties")
public class AccountControllerHeavyST {
    @Value("${server.port}") private int port;
    @Autowired private Logger logger;
    @Autowired private TestRestTemplate testRestTemplate; //TODO Testing REST _client_ @RestClientTest(MyService.class) and @Autowired TestRestTemplate testRestTemplate
    @Autowired private ObjectMapper objectMapper;
    private RequestEntity request;

    @Before
    public void setUpRequest() throws URISyntaxException {
        MultiValueMap<String, String> headers = new HttpHeaders();
        headers.add("X-API-VERSION", "1");
        request = new RequestEntity(
                headers,
                HttpMethod.GET,
                new URI("http://localhost:" + port + "/api" + "/accounts")
        );
    }

    @Test
    public void shouldGetAllAccountWhenUsePrePopulatedFakeDb() {
        //TODO Can be simple but need for headers: testRestTemplate.getForObject(baseUrl + "/accounts", Account[].class);
        ResponseEntity<Account[]> response = testRestTemplate.exchange(request, Account[].class);

        logger.debug(">>>>> ");
        for (Account account : response.getBody()) {
            logger.debug(account.toString());
        }

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).containsOnly(
                new SavingAccount(100, "a@a.ru"),
                new SavingAccount(200, "a@a.ru"),
                new CheckingAccount(100, 100, "b@b.ru"),
                new CheckingAccount(200, 200, "b@b.ru"));
    }
}

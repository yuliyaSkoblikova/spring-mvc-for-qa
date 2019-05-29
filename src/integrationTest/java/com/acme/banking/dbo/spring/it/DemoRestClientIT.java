package com.acme.banking.dbo.spring.it;

import com.acme.banking.dbo.spring.service.AccountCheckService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RunWith(SpringRunner.class)
@RestClientTest(value = AccountCheckService.class)
@ActiveProfiles("integrationTest")
public class DemoRestClientIT {
    @Autowired AccountCheckService accountCheckService;
    @Autowired private MockRestServiceServer mockServer;

    @Test
    public void shouldRespondTrueIfEmailIsValid() {
        mockServer.expect(requestTo("http://emailchecker.ru/email/valid@test.test"))
                .andRespond(withSuccess("valid", MediaType.TEXT_PLAIN));

        boolean isEmailValid = accountCheckService.checkAccountEmail("valid@test.test");

        assertThat(isEmailValid).isTrue();
    }

}

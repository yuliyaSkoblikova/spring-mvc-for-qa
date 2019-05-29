package com.acme.banking.dbo.spring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AccountCheckService {
    @Autowired RestTemplate restTemplate;

    public boolean checkAccountEmail(String email) {
        String emailStatus = restTemplate.getForObject("http://emailchecker.ru/email/" + email, String.class);

        if ("valid".equals(emailStatus)) {
            return true;
        } else {
            return false;
        }
    }
}

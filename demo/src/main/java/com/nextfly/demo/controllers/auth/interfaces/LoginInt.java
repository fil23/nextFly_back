package com.nextfly.demo.controllers.auth.interfaces;

import org.springframework.stereotype.Service;

import lombok.Data;

@Service
public interface LoginInt {

    @Data
    public class ResponseLogin {
        private String token;
        private String messaggio;
    }
}

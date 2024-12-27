package com.nextfly.demo.controllers.auth.interfaces;

import java.util.Map;

import org.springframework.stereotype.Service;

import lombok.Data;

@Service
public interface LoginInt {

    @Data
    public class RequestLogin {
        private Map<String, String> payload;

    }

    @Data
    public class ResponseLogin {
        private String token;
        private String messaggio;
    }
}

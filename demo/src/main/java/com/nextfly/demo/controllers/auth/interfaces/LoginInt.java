package com.nextfly.demo.controllers.auth.interfaces;

import org.springframework.stereotype.Service;

import lombok.Data;

@Service
public interface LoginInt {

    @Data
    public class LoginRequest {
        private String email;
        private String password;
    }

    @Data
    public class LoginResponse {
        private String token;

    }
}

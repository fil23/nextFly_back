package com.nextfly.demo.controllers.auth.interfaces;

import org.springframework.stereotype.Service;

import lombok.Data;

@Service
public interface SignInInt {
    @Data
    public class RequestReg {
        private String email;

    }

    @Data
    public class ResponseValidazione {
        private String msg;
    }

    @Data
    public class RequestVerificaCod {
        private String email;
        private String password;
        private String code;
    }

    @Data
    public class ResponseSignIn {
        private String token;
        private String messaggio;
    }

}

package com.nextfly.demo.db.auth.services;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nextfly.demo.configuration.email.EmailService;
import com.nextfly.demo.configuration.redis.RedisService;
import com.nextfly.demo.controllers.auth.interfaces.SignInInt.RequestReg;
import com.nextfly.demo.controllers.auth.interfaces.SignInInt.ResponseValidazione;

import jakarta.mail.MessagingException;

@Service
public class AuthService {
    final private Logger logger = LoggerFactory.getLogger(AuthService.class);
    final private RedisService redis;
    final private EmailService emailService;

    @Autowired
    public AuthService(RedisService redis, EmailService emailService) {
        this.redis = redis;
        this.emailService = emailService;
    }

    public ResponseValidazione signIn(RequestReg requestReg) {
        ResponseValidazione resp = new ResponseValidazione();
        String cod = emailService.generaCod();
        logger.info("Codice: " + cod);
        redis.save(requestReg.getEmail(), cod, 10, TimeUnit.MINUTES);
        try {
            emailService.sendMail(requestReg.getEmail(), cod);
            resp.setMsg("Email sent");
        } catch (MessagingException e) {
            logger.error("Errore durante l'invio della mail: ", e);
            e.printStackTrace();
        }
        return resp;
    }
}

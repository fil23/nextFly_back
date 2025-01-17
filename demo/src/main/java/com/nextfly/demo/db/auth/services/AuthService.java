package com.nextfly.demo.db.auth.services;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Date;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.nextfly.demo.configuration.email.EmailService;
import com.nextfly.demo.configuration.redis.RedisService;
import com.nextfly.demo.controllers.auth.interfaces.SignInInt.RequestReg;
import com.nextfly.demo.controllers.auth.interfaces.SignInInt.RequestVerificaCod;
import com.nextfly.demo.controllers.auth.interfaces.SignInInt.ResponseSignIn;
import com.nextfly.demo.controllers.auth.interfaces.SignInInt.ResponseValidazione;
import com.nextfly.demo.db.token.services.JwtService;
import com.nextfly.demo.db.utente.entities.UtenteEntity;
import com.nextfly.demo.db.utente.service.UtenteService;

import jakarta.mail.MessagingException;

@Service
public class AuthService {
    final private Logger logger = LoggerFactory.getLogger(AuthService.class);
    final private RedisService redis;
    final private EmailService emailService;
    final private UtenteService utenteService;
    final private JwtService tokenService;
    final private PasswordEncoder passwordEncoder;

    @Autowired
    public AuthService(RedisService redis, EmailService emailService, UtenteService utenteService,
            JwtService tokenService, PasswordEncoder passwordEncoder) {
        this.redis = redis;
        this.emailService = emailService;
        this.utenteService = utenteService;
        this.tokenService = tokenService;
        this.passwordEncoder = passwordEncoder;
    }

    public ResponseValidazione velidateEmail(RequestReg requestReg) {
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

    public ResponseSignIn validateCode(RequestVerificaCod request)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        ResponseSignIn response = new ResponseSignIn();
        if (redis.getEntity(request.getEmail()).equals(request.getCode())) {
            UtenteEntity utente = new UtenteEntity();
            utente.setEmail(request.getEmail());
            utente.setPassword(passwordEncoder.encode(request.getPassword()));
            utente.setDataCre(new Date(System.currentTimeMillis()));
            utenteService.salvaUtente(utente);
            String token = tokenService.buildToken(utente);
            response.setToken(token);
            response.setMessaggio("Token generation successfull");
        } else {
            response.setToken(null);
            response.setMessaggio("Wrong code");
        }
        return response;
    }
}

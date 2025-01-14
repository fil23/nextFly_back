package com.nextfly.demo.db.utente.service;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nextfly.demo.controllers.auth.interfaces.SignInInt;
import com.nextfly.demo.controllers.auth.interfaces.SignInInt.ResponseSignIn;
import com.nextfly.demo.db.token.services.JwtService;
import com.nextfly.demo.db.utente.entities.UtenteEntity;
import com.nextfly.demo.db.utente.repositories.UtenteRepo;

@Service
public class UtenteService {
    final private UtenteRepo utenteRepo;
    final private JwtService jwt;

    @Autowired
    public UtenteService(UtenteRepo utenteRepo, JwtService jwt) {
        this.utenteRepo = utenteRepo;
        this.jwt = jwt;
    }

    public SignInInt.ResponseSignIn salvaUtente(UtenteEntity utente)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        ResponseSignIn response = new ResponseSignIn();
        utenteRepo.save(utente);

        response.setToken(jwt.buildToken(utente));
        response.setMessaggio("Token creato con successo");

        return response;

    }
}

package com.nextfly.demo.db.utente.service;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.nextfly.demo.controllers.auth.interfaces.LoginInt;
import com.nextfly.demo.controllers.auth.interfaces.SignInInt;
import com.nextfly.demo.controllers.auth.interfaces.LoginInt.LoginResponse;
import com.nextfly.demo.controllers.auth.interfaces.SignInInt.ResponseSignIn;
import com.nextfly.demo.db.token.services.JwtService;
import com.nextfly.demo.db.utente.entities.UtenteEntity;
import com.nextfly.demo.db.utente.repositories.UtenteRepo;

@Service
public class UtenteService {
    final private UtenteRepo utenteRepo;
    final private JwtService jwt;
    final private PasswordEncoder passwordEncoder;

    @Autowired
    public UtenteService(UtenteRepo utenteRepo, JwtService jwt, PasswordEncoder passwordEncoder) {
        this.utenteRepo = utenteRepo;
        this.jwt = jwt;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * salvataggio utente su DB
     * 
     * @param utente
     * @return token + messaggio
     *
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public SignInInt.ResponseSignIn salvaUtente(UtenteEntity utente)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        ResponseSignIn response = new ResponseSignIn();
        utenteRepo.save(utente);

        response.setToken(jwt.buildToken(utente));
        response.setMessaggio("Token creato con successo");

        return response;

    }

    /**
     * Ricerca utente in DB e creazione token
     * 
     * @param request (email + password)
     * @return LoginResponse (token)
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public LoginInt.LoginResponse getUtenteLogin(LoginInt.LoginRequest request)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        LoginResponse response = new LoginResponse();
        Optional<UtenteEntity> utenteEntity = utenteRepo.findUtenteEntityByEmail(request.getEmail());
        if (utenteEntity.isPresent()
                && passwordEncoder.matches(request.getPassword(), utenteEntity.get().getPassword())) {

            response.setToken(jwt.buildToken(utenteEntity.get()));
        } else {
            response.setToken(null);
        }

        return response;

    }

    public boolean utenteExist(String email) {
        return utenteRepo.existsByEmail(email);

    }
}

package com.nextfly.demo.controllers.auth;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.nextfly.demo.controllers.auth.interfaces.LoginInt;
import com.nextfly.demo.controllers.auth.interfaces.LoginInt.RequestLogin;
import com.nextfly.demo.controllers.auth.interfaces.LoginInt.ResponseLogin;
import com.nextfly.demo.db.utente.entities.UtenteEntity;
import com.nextfly.demo.db.utente.service.UtenteService;

import java.sql.Date;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping(name = "/api/auth")
public class AuthController {

    final private UtenteService utenteService;

    @Autowired
    public AuthController(UtenteService utenteService) {
        this.utenteService = utenteService;
    }

    @PostMapping("/google")
    public ResponseEntity<LoginInt.ResponseLogin> authenticationWithGoogle(@RequestBody RequestLogin request) {
        String idToken = request.getPayload().get("token");

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(),
                GsonFactory.getDefaultInstance())
                .setAudience(Collections
                        .singletonList("591853949644-430fhsmkongm5t549cpi8nl15dqca3a0.apps.googleusercontent.com"))
                .build();
        ResponseLogin response = new ResponseLogin();
        try {
            GoogleIdToken googleIdToken = verifier.verify(idToken);
            if (googleIdToken != null) {
                GoogleIdToken.Payload payload2 = googleIdToken.getPayload();
                String email = payload2.getEmail();
                UtenteEntity utente = new UtenteEntity();
                utente.setDataCre(new Date(System.currentTimeMillis()));
                utente.setEmail(email);
                utente.setPassword(idToken);
                response = utenteService.salvaUtente(utente);
                return new ResponseEntity<LoginInt.ResponseLogin>(response, HttpStatus.CREATED);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<LoginInt.ResponseLogin>(response, HttpStatus.CREATED);
    }

}

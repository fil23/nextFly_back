package com.nextfly.demo.controllers.auth;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.nextfly.demo.configuration.email.EmailService;
import com.nextfly.demo.configuration.redis.RedisService;
import com.nextfly.demo.controllers.auth.interfaces.LoginInt;
import com.nextfly.demo.controllers.auth.interfaces.SignInInt;
import com.nextfly.demo.controllers.auth.interfaces.SignInInt.ResponseSignIn;
import com.nextfly.demo.controllers.auth.interfaces.SignInInt.ResponseValidazione;
import com.nextfly.demo.db.auth.services.AuthService;
import com.nextfly.demo.db.utente.entities.UtenteEntity;
import com.nextfly.demo.db.utente.service.UtenteService;

import java.sql.Date;
import java.util.Collections;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final Logger logger = LoggerFactory.getLogger(AuthController.class);
    final private UtenteService utenteService;
    final private AuthService authService;

    @Autowired
    public AuthController(UtenteService utenteService, AuthService authService) {
        this.utenteService = utenteService;
        this.authService = authService;

    }

    @GetMapping("/prova")
    public String getMethodName() {
        return "ok";
    }

    @PostMapping(value = "/google")
    public ResponseEntity<SignInInt.ResponseSignIn> authenticationWithGoogle(@RequestBody Map<String, String> payload) {
        String idToken = payload.get("token");
        logger.info(("Sono dentro /google"));
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(),
                GsonFactory.getDefaultInstance())
                .setAudience(Collections
                        .singletonList("591853949644-430fhsmkongm5t549cpi8nl15dqca3a0.apps.googleusercontent.com"))
                .build();
        ResponseSignIn response = new ResponseSignIn();
        try {
            GoogleIdToken googleIdToken = verifier.verify(idToken);
            if (googleIdToken != null) {
                GoogleIdToken.Payload payload2 = googleIdToken.getPayload();
                String email = payload2.getEmail();
                String pws = payload.get("password");
                UtenteEntity utente = new UtenteEntity();
                utente.setDataCre(new Date(System.currentTimeMillis()));
                utente.setEmail(email);
                utente.setPassword(pws);
                response = utenteService.salvaUtente(utente);
                return new ResponseEntity<SignInInt.ResponseSignIn>(response, HttpStatus.CREATED);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<SignInInt.ResponseSignIn>(response, HttpStatus.CREATED);
    }

    @PostMapping("/verifica_email_reg")
    public ResponseEntity<SignInInt.ResponseValidazione> verifyEmail(@RequestBody SignInInt.RequestReg request) {
        logger.info("------------ STO ESEGUENDO:  " + getMethodName() + "---------------------");
        ResponseValidazione response = authService.signIn(request);
        return new ResponseEntity<SignInInt.ResponseValidazione>(response,
                response.getMsg() != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

}

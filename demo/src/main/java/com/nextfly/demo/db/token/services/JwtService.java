package com.nextfly.demo.db.token.services;

import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.token.TokenService;
import org.springframework.stereotype.Service;

import com.nextfly.demo.db.chiavi.service.ChiaviService;
import com.nextfly.demo.db.utente.entities.UtenteEntity;
import com.nextfly.demo.db.utente.repositories.UtenteRepo;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JwtService {
    private ChiaviService chiaviService;
    private UtenteRepo utenteRepo;

    @Autowired
    public JwtService(TokenService tokenService, UtenteRepo utenteRepo) {
        this.chiaviService = chiaviService;
        this.utenteRepo = utenteRepo;
    }

    public String buildToken(UtenteEntity user)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        Map<String, Object> claim = new HashMap();
        claim.put("email", user.getEmail());
        String id = Long.toString(user.getId());
        return Jwts
                .builder()
                .setClaims(claim)
                .setSubject(id)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .signWith(getSignInKey(), SignatureAlgorithm.RS256)
                .compact();
    }

    public boolean isTokenValid(String token) throws Exception {
        PrivateKey privatekey = getSignInKey();
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(privatekey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            Optional<UtenteEntity> utente = utenteRepo.findById(Long.valueOf(claims.getSubject()));
            if (utente.isPresent() && utente.get().getEmail().equals(claims.get("email"))) {
                return true;
            }
        } catch (Exception e) {
            throw new RuntimeException("Token non valido o compromesso", e);
        }

        return false;

    }

    public String extractUsername(String token) throws NoSuchAlgorithmException, InvalidKeySpecException {
        Claims allClaims = extractAllClaims(token);
        return (String) allClaims.get("username");
    }

    public String extractEmail(String token) throws NoSuchAlgorithmException, InvalidKeySpecException {
        return extractAllClaims(token).getSubject();
    }

    private Claims extractAllClaims(String token) throws NoSuchAlgorithmException, InvalidKeySpecException {
        return Jwts
                .parserBuilder()
                .setSigningKey(getPublicKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private PrivateKey getSignInKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
        return chiaviService.getPrivateKey();
    }

    private PublicKey getPublicKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
        return chiaviService.getPublicKey();
    }
}

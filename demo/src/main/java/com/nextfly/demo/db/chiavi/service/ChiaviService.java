package com.nextfly.demo.db.chiavi.service;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.api.client.util.Value;
import com.nextfly.demo.db.chiavi.entities.ChiaviEntity;
import com.nextfly.demo.db.chiavi.repositories.ChiaviRepo;

@Service
public class ChiaviService {
    @Value("${app.token.secret}")
    private String secret;

    @Autowired
    private ChiaviRepo repo;

    public void generaChiavi() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048, new SecureRandom()); // Lunghezza della chiave: 2048 bit
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        // Estrai chiavi pubblica e privata
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();

        // Codifica in Base64 per salvarle o visualizzarle
        String publicKeyBase64 = Base64.getEncoder().encodeToString(publicKey.getEncoded());
        String privateKeyBase64 = Base64.getEncoder().encodeToString(privateKey.getEncoded());
        ChiaviEntity key = new ChiaviEntity();

        key.setDescrizione("chiave_pbl");
        key.setPbl_key(publicKeyBase64);
        repo.save(key);

        ChiaviEntity k = new ChiaviEntity();
        k.setDescrizione("chiave_prv");
        k.setPbl_key(privateKeyBase64);
        repo.save(k);

        System.out.println("Chiave pubblica:");
        System.out.println(publicKeyBase64);
        System.out.println("\nChiave privata:");
        System.out.println(privateKeyBase64);
    }

    public PublicKey getPublicKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
        String key = repo.findChiaviEntityByDescrizione("chiave_pbl").getPbl_key();
        byte[] keyBytes = java.util.Base64.getDecoder().decode(key);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(spec);

    }

    public PrivateKey getPrivateKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
        String key = repo.findChiaviEntityByDescrizione("chiave_prv").getPbl_key();
        byte[] keyBytes = java.util.Base64.getDecoder().decode(key);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(spec);
    }
}

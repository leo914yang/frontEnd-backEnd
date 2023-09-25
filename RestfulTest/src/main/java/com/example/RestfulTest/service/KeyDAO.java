package com.example.RestfulTest.service;

import com.example.RestfulTest.dao.model.KeyEntity;
import com.example.RestfulTest.dao.vo.KeyEntityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Service
@Slf4j
public class KeyDAO {
    @Autowired
    private KeyEntityRepository keyEntityRepository;
    public void saveKey(int size, String token, PublicKey publicKey, PrivateKey privateKey){
        log.info("saveKey publicKey: " + publicKey);
        KeyEntity keyEntity = new KeyEntity();
        keyEntity.setId(size);
        keyEntity.setToken(token);
        String stringPublic = convertKeyToString(publicKey);
        String stringPrivate = convertKeyToString(privateKey);
        keyEntity.setPublickey(stringPublic);
        keyEntity.setPrivatekey(stringPrivate);
        keyEntityRepository.save(keyEntity);
    }

    public String convertKeyToString(PrivateKey privateKey) {
        byte[] privateKeyBytes = privateKey.getEncoded();
        return Base64.getEncoder().encodeToString(privateKeyBytes);
    }

    public String convertKeyToString(PublicKey publicKey) {
        byte[] publicKeyKeyBytes = publicKey.getEncoded();
        return Base64.getEncoder().encodeToString(publicKeyKeyBytes);
    }

    public PrivateKey convertStringToPrivateKey(String privateKeyStr) {
        byte[] privateKeyBytes = Base64.getDecoder().decode(privateKeyStr);
        KeyFactory keyFactory = null;
        PrivateKey privateKey = null;
        try {
            keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(privateKeyBytes); // 使用X509EncodedKeySpec
            privateKey = keyFactory.generatePrivate(keySpec);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return privateKey;
    }

    public PublicKey convertStringToPublicKey(String publicKeyStr) {
        byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyStr);
        KeyFactory keyFactory = null;
        PublicKey publicKey = null;
        try {
            keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes); // 使用X509EncodedKeySpec
            publicKey = keyFactory.generatePublic(keySpec);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return publicKey;
    }
}

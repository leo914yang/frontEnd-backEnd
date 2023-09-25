package com.example.RestfulTest.service;

import com.example.RestfulTest.dao.model.KeyEntity;
import com.example.RestfulTest.dao.model.MyEntity;
import com.example.RestfulTest.dao.vo.KeyEntityRepository;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Slf4j
@Component
public class TokenGenerator {
    @Autowired
    private KeyDAO keyDAO;
    @Autowired
    private KeyEntityRepository keyEntityRepository;
    public String generateJwtToken(MyEntity myEntity) {
        // 生成RSA密钥对
        KeyPair keyPair = null;
        try {
            keyPair = generateRSAKeyPair();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // 获取私钥和公钥
        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey publicKey = keyPair.getPublic();

        // 创建JWT令牌并签名
        String jwtToken = Jwts.builder()
                .setSubject("success")
                .claim("name", myEntity.getName()) // 将名称添加到令牌负载
                .claim("identity", myEntity.getIdentity()) // 将身份信息添加到令牌负载
                .claim("birth", myEntity.getBirth()) // 将生日添加到令牌负载
                .claim("phone", myEntity.getPhone()) // 将电话信息添加到令牌负载
                .setExpiration(myEntity.getTime())
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .compact();

        keyDAO.saveKey(myEntity.getId(), jwtToken, publicKey, privateKey);
        JsonObject jsonData = new JsonObject();
        jsonData.addProperty("token", jwtToken);
        jsonData.addProperty("publicKey", keyDAO.convertKeyToString(publicKey));
        Gson gson = new Gson();
        return gson.toJson(jsonData);
    }

    private static KeyPair generateRSAKeyPair() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048); // 设置RSA密钥长度
        return keyPairGenerator.generateKeyPair();
    }

    public boolean verifyToken(String token){
        KeyEntity keyEntity = keyEntityRepository.findByToken(token);
        PublicKey publicKey = keyDAO.convertStringToPublicKey(keyEntity.getPublickey());
        // 驗證JWT令牌
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(publicKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String subject = claims.getSubject();
            return subject != null;
        } catch (Exception e) {
            log.error("JWT verification failed: " + e.getMessage(), e);
            return false;
        }
    }
}

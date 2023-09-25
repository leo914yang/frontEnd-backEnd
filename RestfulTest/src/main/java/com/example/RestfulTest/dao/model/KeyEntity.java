package com.example.RestfulTest.dao.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.security.PrivateKey;
import java.security.PublicKey;

@Entity
@Data
@Table(name = "keytable")
public class KeyEntity {
    @Id
    private int id;

    private String token;

    private String publickey;

    private String privatekey;
}

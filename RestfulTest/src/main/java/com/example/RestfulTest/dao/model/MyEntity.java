package com.example.RestfulTest.dao.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Data
@Table(name = "mytable")
public class MyEntity implements Serializable {
    @Id
    private int id;

    private String name;

    private String des;

    private Date time;

    private String identity;

    private String birth;

    private String phone;

    private String token;

    private String permission;
}

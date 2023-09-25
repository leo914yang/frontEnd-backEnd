package com.example.RestfulTest.dao.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "newstable")
public class NewsEntity {
    @Id
    private int id;

    private String source;

    private String url;

    private String title;
}

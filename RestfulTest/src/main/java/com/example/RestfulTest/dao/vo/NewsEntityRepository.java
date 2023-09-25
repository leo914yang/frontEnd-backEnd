package com.example.RestfulTest.dao.vo;

import com.example.RestfulTest.dao.model.NewsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewsEntityRepository extends JpaRepository<NewsEntity, Integer> {
    NewsEntity findById(int id);
}

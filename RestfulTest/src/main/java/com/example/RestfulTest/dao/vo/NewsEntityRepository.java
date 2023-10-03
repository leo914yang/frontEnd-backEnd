package com.example.RestfulTest.dao.vo;

import com.example.RestfulTest.dao.model.NewsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewsEntityRepository extends JpaRepository<NewsEntity, Integer> {
    @Query(value = "SELECT * FROM newstable LIMIT ?1", nativeQuery = true)
    List<NewsEntity> findLimit(int number);
}

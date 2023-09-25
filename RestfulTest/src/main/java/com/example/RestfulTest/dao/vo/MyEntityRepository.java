package com.example.RestfulTest.dao.vo;

import com.example.RestfulTest.dao.model.MyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Repository
public interface MyEntityRepository extends JpaRepository<MyEntity, Integer> {
    @Query(value = "SELECT * FROM mytable WHERE name = ?1", nativeQuery = true)
    List<MyEntity> findByName(String name);
    MyEntity findById(int Id);
    @Query(value = "SELECT * FROM mytable WHERE time < ?1", nativeQuery = true)
    List<MyEntity> findExpired(Date time);
}

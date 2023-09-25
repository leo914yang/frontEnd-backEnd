package com.example.RestfulTest.dao.vo;

import com.example.RestfulTest.dao.model.KeyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface KeyEntityRepository extends JpaRepository<KeyEntity, Integer> {
    KeyEntity findByToken(String token);
    @Query(value = "SELECT id FROM keytable WHERE token = ?1 AND id IN (SELECT id FROM mytable WHERE token = ?1)", nativeQuery = true)
    Integer findTokenId(String token);
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM keytable WHERE id IN :ids", nativeQuery = true)
    void deleteByIds(List<Integer> ids);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM keytable WHERE id = :id", nativeQuery = true)
    void deleteByIds(int id);

}

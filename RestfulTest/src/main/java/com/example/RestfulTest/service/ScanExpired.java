package com.example.RestfulTest.service;

import com.example.RestfulTest.dao.model.MyEntity;
import com.example.RestfulTest.dao.vo.KeyEntityRepository;
import com.example.RestfulTest.dao.vo.MyEntityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class ScanExpired {
    @Autowired
    private MyEntityRepository myEntityRepository;
    @Autowired
    private KeyEntityRepository keyEntityRepository;
    @Scheduled(fixedRate = 30000)
    public void scanStart(){
        log.info("開始掃描");
        LocalDateTime currentTime = LocalDateTime.now();
        Instant instant = currentTime.atZone(ZoneId.systemDefault()).toInstant();
        Date now = Date.from(instant);
        List<MyEntity> expired = myEntityRepository.findExpired(now);
        List<Integer> ids = new ArrayList<>();
        expired.stream()
                .map(MyEntity::getId)
                .forEach(ids::add);
        keyEntityRepository.deleteByIds(ids);
        myEntityRepository.deleteAll(expired);
        log.info("掃描結束，移除了" + expired.size() + "筆過期資料");
    }
}

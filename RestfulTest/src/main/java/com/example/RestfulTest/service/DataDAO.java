package com.example.RestfulTest.service;

import com.example.RestfulTest.dao.model.MyEntity;
import com.example.RestfulTest.dao.model.NewsEntity;
import com.example.RestfulTest.dao.vo.MyEntityRepository;
import com.example.RestfulTest.dao.vo.NewsEntityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class DataDAO {
    @Autowired
    private MyEntityRepository myEntityRepository;
    @Autowired
    private TokenGenerator tokenGenerator;
    @Autowired
    private NewsEntityRepository newsEntityRepository;

    public String saveUser(Map<String, String> jsonFile) {
        List<MyEntity> idSeq = myEntityRepository.findAll();
        int size = idSeq.size();
        MyEntity myEntity = new MyEntity();
        myEntity.setName(jsonFile.get("name"));
        myEntity.setDes(jsonFile.get("des"));
        myEntity.setIdentity(jsonFile.get("id"));
        myEntity.setBirth(jsonFile.get("birth"));
        myEntity.setPhone(jsonFile.get("phone"));
        // TIME================
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime newTime = currentTime.plusSeconds(100);
        Instant instant = newTime.atZone(ZoneId.systemDefault()).toInstant();
        Date expirationDate = Date.from(instant);
        myEntity.setTime(expirationDate);
        log.info("寫入資料庫時間: " + expirationDate);
        // TIME================
        myEntity.setId(size + 1);
        String token = tokenGenerator.generateJwtToken(myEntity);
        myEntity.setToken(token);
        myEntityRepository.save(myEntity);
        return token;
    }

    public void saveNews(List<String> hrefList, List<String> textList) {
        List<NewsEntity> idList = newsEntityRepository.findAll();
        int id = idList.size() + 1;
        List<NewsEntity> newsEntityList = new ArrayList<>();
        for (int i = 0; i < hrefList.size(); i++) {
            NewsEntity newsEntity = new NewsEntity();
            newsEntity.setId(Math.max(i + 1, id + i));
            newsEntity.setUrl(hrefList.get(i));
            newsEntity.setTitle(textList.get(i));
            newsEntityList.add(newsEntity);
            log.info("newsEntity: " + newsEntity);
        }
        newsEntityRepository.saveAll(newsEntityList);
    }

}

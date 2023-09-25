package com.example.RestfulTest.service;

import com.example.RestfulTest.config.MyJwtInterceptor;
import com.example.RestfulTest.dao.model.MyEntity;
import com.example.RestfulTest.dao.vo.KeyEntityRepository;
import com.example.RestfulTest.dao.vo.MyEntityRepository;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/")
public class MyCrud {
    @Autowired
    private MyEntityRepository myEntityRepository;
    @Autowired
    private TokenGenerator tokenGenerator;
    @Autowired
    private DataDAO dataDAO;
    @Autowired
    private MyJwtInterceptor myJwtInterceptor;
    @Autowired
    private KeyEntityRepository keyEntityRepository;
    @Autowired
    private WebScraperService webScraperService;

    @PostMapping("/api")
    public String create(@RequestBody Map<String, String> jsonFile){
        return dataDAO.saveUser(jsonFile);
    }

    @GetMapping("/api")
    public ResponseEntity<String> read(@RequestParam("name") String name){
        List<MyEntity> allData = myEntityRepository.findByName(name);
        Gson gson = new Gson();
        return ResponseEntity.ok(gson.toJson(allData));
    }

    @PutMapping("/api")
    public String update(HttpServletRequest request,
                         @RequestHeader(name = "Authorization", required = false) String authorizationHeader,
                         @RequestBody Map<String, String> jsonFile){
        if(authorizationHeader.isEmpty()){
            return "Failed";
        }
        boolean checkValid = myJwtInterceptor.check(request, authorizationHeader);
        if(!checkValid){
            return "Failed";
        }
        MyEntity myEntity = myEntityRepository.findById(Integer.parseInt(jsonFile.get("id")));
        myEntity.setDes(jsonFile.get("des"));
        myEntityRepository.save(myEntity);
        return "Success";

    }

    @DeleteMapping("/api")
    public String delete(HttpServletRequest request,
                         @RequestHeader(name = "Authorization", required = false) String authorizationHeader,
                         @RequestBody Map<String, String> jsonFile){
        boolean checkValid = myJwtInterceptor.check(request, authorizationHeader);
        if(!checkValid){
            return "Failed";
        }
        MyEntity myEntity = myEntityRepository.findById(Integer.parseInt(jsonFile.get("id")));
        String deletedToken = myEntity.getToken();
        int deletedId = myEntity.getId();
        keyEntityRepository.deleteByIds(deletedId);
        myEntityRepository.delete(myEntity);
        log.info("deletedToken: " + deletedToken);
        return deletedToken;
    }

    @PostMapping("/api/news")
    public String news(@RequestBody Map<String, String> jsonFile){
        Gson gson = new Gson();
        log.info("Test MyCRUD");
        return gson.toJson(webScraperService.scrapeWebsite(jsonFile));
    }
    @PostMapping("/api/newsSave")
    public String newsSave(@RequestBody Map<String, List<String>> jsonFile){
        return webScraperService.savePages(jsonFile);
    }

}

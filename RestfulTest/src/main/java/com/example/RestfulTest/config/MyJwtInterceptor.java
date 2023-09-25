package com.example.RestfulTest.config;

import com.example.RestfulTest.service.TokenGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestHeader;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Component
public class MyJwtInterceptor {
    @Autowired
    private TokenGenerator tokenGenerator;

    public boolean check(HttpServletRequest request,
                         @RequestHeader("Authorization") String authorizationHeader){
        String method = request.getMethod();
        if ("PUT".equals(method) || "DELETE".equals(method)) {
            log.info("authorizationHeader: " + authorizationHeader);
            if (authorizationHeader == null ) {
                return false;
            }
            return tokenGenerator.verifyToken(authorizationHeader);
        }
        return true;
    }
}


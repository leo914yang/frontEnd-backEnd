package com.example.RestfulTest.config;

import com.example.RestfulTest.service.ScanExpired;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class ScheduleConfig {
    @Autowired
    private ScanExpired scanExpired;
}

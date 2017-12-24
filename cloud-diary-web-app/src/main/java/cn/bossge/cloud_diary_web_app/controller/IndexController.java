package cn.bossge.cloud_diary_web_app.controller;

import java.time.LocalDateTime;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {

    @GetMapping("/")
    public String index(){
        return "App is running. "+LocalDateTime.now();
    }
    
}

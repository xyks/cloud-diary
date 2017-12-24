package cn.bossge.cloud_diary_web_app.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

    Resource loadAsResource(String fileName);
    
    void store(MultipartFile file,  String path);
}

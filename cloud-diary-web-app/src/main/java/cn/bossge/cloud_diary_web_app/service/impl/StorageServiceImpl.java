package cn.bossge.cloud_diary_web_app.service.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import cn.bossge.cloud_diary_web_app.constant.DiaryMessage;
import cn.bossge.cloud_diary_web_app.exception.UserException;
import cn.bossge.cloud_diary_web_app.service.StorageService;
import lombok.extern.log4j.Log4j2;
@Log4j2
@Service
public class StorageServiceImpl implements StorageService{
    
    @Value("${file.directory}")
    String directory;
    @Autowired
    private ApplicationContext appContext;
    
    @Override
    public Resource loadAsResource(String fileName) {
        Path path = Paths.get(directory+fileName);
        Resource resource = appContext.getResource("file:"+path);
        return resource;
    }

    @Override
    public void store(MultipartFile file, String path) {
        try {
            byte[] bytes = file.getBytes();
            Path location = Paths.get(directory+path);
            Files.write(location, bytes);
        } catch (IOException e) {
           log.error("Failed to save file.",e);
           throw new UserException(DiaryMessage.UPLOAD_FILE_ERROR);
        }
        
    }

}

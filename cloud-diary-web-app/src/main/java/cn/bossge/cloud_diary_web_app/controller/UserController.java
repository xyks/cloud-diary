package cn.bossge.cloud_diary_web_app.controller;

import java.time.Instant;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import cn.bossge.cloud_diary_common.dto.ResponseDTO;
import cn.bossge.cloud_diary_common.service.TokenService;
import cn.bossge.cloud_diary_web_app.constant.DiaryMessage;
import cn.bossge.cloud_diary_web_app.dto.UserDTO;
import cn.bossge.cloud_diary_web_app.exception.UserException;
import cn.bossge.cloud_diary_web_app.service.StorageService;
import cn.bossge.cloud_diary_web_app.service.UserService;
import io.jsonwebtoken.Claims;
import lombok.extern.log4j.Log4j2;
@Log4j2
@RestController
public class UserController {

    
    @Value("${file.image}")
    String IMAGE_DIRECTORY;
    
    @Autowired
    UserService userService;
    
    @Autowired
    StorageService storageService;
    
    @CrossOrigin
    @PostMapping("user/basic")
    public ResponseDTO updateBasicInfo(@RequestBody UserDTO userDTO, @RequestHeader String token){
        Claims body = TokenService.parseToken(token);
        userDTO.setId(Long.valueOf(body.getId()));
        userService.updateBasicInfo(userDTO);
        userDTO.setId(null);
        return  ResponseDTO.builder().success(true).message(DiaryMessage.USER_BASIC_UPDATE_SUCCESS).content(userDTO).build();
    }

    @CrossOrigin
    @GetMapping("user")
    public ResponseDTO get(@RequestHeader String token){
        Claims body = TokenService.parseToken(token);
        UserDTO user = userService.get(Long.valueOf(body.getId()));
        user.setId(null);
        user.setEmail(body.getSubject());
        return  ResponseDTO.builder().success(true).message(DiaryMessage.USER_DETAIL_GET_SUCCESS).content(user).build();
    }
    
    @CrossOrigin
    @PostMapping("user/image/upload")
    public ResponseDTO uploadImage(@RequestParam MultipartFile file,@RequestParam String imageType, @RequestParam String token){
        Claims body = TokenService.parseToken(token);
        if (file.isEmpty()) {
           throw new UserException(DiaryMessage.UPLOAD_FILE_NOT_LEGAL); 
        }
        String id = body.getId();
        String imagePath = Instant.now().getEpochSecond()+"-"+file.getOriginalFilename();
        String path = IMAGE_DIRECTORY+id+"-"+imagePath;
        try {
            storageService.store(file, path);
        } catch (Exception e) {
            throw new UserException(e.getMessage());
        }
        
        
        UserDTO userDTO = userService.updateImage(imagePath,imageType, Long.valueOf(id));
        return  ResponseDTO.builder().success(true).message(DiaryMessage.UPLOAD_IMAGE_SUCCESS).content(userDTO).build();
    }
    
    @CrossOrigin
    @GetMapping("/user/image/{fileName:.+}")
    public ResponseEntity<Resource> serveFile(@PathVariable String fileName,@RequestParam String token) {
        Claims body = TokenService.parseToken(token);
        String id = body.getId();
        String actualFileName =  IMAGE_DIRECTORY+id+"-"+fileName;
        Resource file = storageService.loadAsResource(actualFileName);
        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""+file.getFilename()+"\"")
                .body(file);
    }
    
    
}

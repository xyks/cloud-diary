package cn.bossge.cloud_diary_web_app.service.impl;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.InitBinder;

import cn.bossge.cloud_diary_web_app.constant.DiaryMessage;
import cn.bossge.cloud_diary_web_app.dto.UserDTO;
import cn.bossge.cloud_diary_web_app.entity.User;
import cn.bossge.cloud_diary_web_app.exception.UserException;
import cn.bossge.cloud_diary_web_app.repository.UserRepository;
import cn.bossge.cloud_diary_web_app.service.UserService;
import lombok.extern.log4j.Log4j2;

import java.lang.reflect.InvocationTargetException;

import javax.annotation.PostConstruct;


@Service
@Log4j2
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Override
    public void updateBasicInfo(UserDTO userDTO) {
        Long id = userDTO.getId();
        User user = userRepository.findOne(id);
        if (user == null){
            user = new User();
            user.setId(id);
        }
        user.setNickName(userDTO.getNickName());
        user.setSignature(userDTO.getSignature());
        user.setGender(userDTO.getGender());
        userRepository.save(user);
    }

    @Override
    public UserDTO update(UserDTO userDTO) {
/*        Long id = userDTO.getId();
        User user = userRepository.findOne(id);
        if (user == null){
            user = User.builder().id(id).build();

        }
        user.setNickName(userDTO.getNickName());
        user.setSignature(userDTO.getSignature());
        user.setGender(userDTO.getGender());
        userRepository.save(user);*/
        return null;
    }

    @Override
    public UserDTO get(Long id) {
        User user = userRepository.findOne(id);
        UserDTO result = new UserDTO();
        if (user == null){
            user = User.builder().id(id).nickName("Me").signature("Welcome to Mini Diary").build();
            userRepository.save(user);
        }
        try {
            BeanUtils.copyProperties(result, user);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public int checkKeyCode(String keyCode, Long userId) {
        User user = userRepository.findOne(userId);
        if (user != null){
            if (StringUtils.isNotBlank(user.getKeyCode())){
                if (!user.getKeyCode().equals(keyCode)){
                    return -1;
                }else {
                    return 0;
                }
            }else{
                return 1;
            }
        }else {
           return -1;
        }
    }

    @Override
    public UserDTO updateImage(String imagePath, String imageType, Long id) {
    
        User user = userRepository.findOne(id);
        if ("headPortrait".equals(imageType)) {
            user.setHeadPortrait(imagePath);
        }else if("cover".equals(imageType)) {
            user.setCover(imagePath);
        }
        userRepository.save(user);
        return UserDTO.builder().cover(user.getCover()).headPortrait(user.getHeadPortrait()).build();
    }

    @Override
    public void saveKeyCode(String keyCode, Long id) {
        User user = userRepository.findOne(id);
        if (user != null){
           user.setKeyCode(keyCode);
           userRepository.save(user);
        }else {
           throw new UserException(DiaryMessage.USER_NOT_FOUND);
        }
        
    }
}

package cn.bossge.cloud_diary_web_app.service;

import cn.bossge.cloud_diary_web_app.dto.UserDTO;


public interface UserService {

    void updateBasicInfo(UserDTO userDTO);
    UserDTO update(UserDTO user);

    UserDTO get(Long aLong);
    /**
     * 
     * @param keyCode
     * @param userId
     * @return 0 OK  1 have not set key code -1 error
     */
    int checkKeyCode(String keyCode, Long userId);
    UserDTO updateImage(String imagePath, String imageType, Long id);
    void saveKeyCode(String keyCode, Long id);
}

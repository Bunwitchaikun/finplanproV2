package com.finplanpro.finplanpro.service;

import com.finplanpro.finplanpro.dto.UserDto;
import com.finplanpro.finplanpro.entity.User;

public interface UserService {
    void saveUser(UserDto userDto);

    User findUserByUsername(String username);

    User findUserByEmail(String email);

    // เพิ่มเมธอดนี้เข้าไป
    boolean validateUser(String username, String rawPassword);
}

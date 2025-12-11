package com.spring.demo.service.user;

import com.spring.demo.entity.ResultVO;
import com.spring.demo.entity.User;

public interface UserService {

    ResultVO addUser(User user);
    ResultVO listAllUsers();
    ResultVO getUserById(Long id);
    ResultVO updateUser(User user);
    ResultVO deleteUser(Long id);
}
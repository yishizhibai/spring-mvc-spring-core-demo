package com.spring.demo.service.user;

import com.spring.demo.entity.PageRequest;
import com.spring.demo.entity.ResultVO;
import com.spring.demo.entity.User;

public interface UserService {
    // 原有方法保留
    ResultVO addUser(User user);
    ResultVO listAllUsers();
    ResultVO getUserById(String id);
    ResultVO updateUser(User user);
    ResultVO deleteUser(String id);
    ResultVO listUserByPage(PageRequest pageRequest);
    ResultVO searchUserByUsername(String usernameKeyword);
}
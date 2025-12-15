package com.spring.demo.service.user;

import com.spring.demo.entity.ResultVO;
import com.spring.demo.entity.User;

public interface UserService {
    ResultVO addUser(User user);
    ResultVO listAllUsers();
    ResultVO getUserById(String id); // 将Long改为String（与实体、数据库id类型一致）
    ResultVO updateUser(User user);
    ResultVO deleteUser(String id); // 将Long改为String
}
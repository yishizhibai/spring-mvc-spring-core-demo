package com.spring.demo.service.user;

import com.spring.demo.entity.ResultVO;
import com.spring.demo.entity.User;

public interface UserService {
    /**
     * 新增用户
     */
    ResultVO addUser(User user);

    /**
     * 查询所有用户
     */
    ResultVO listAllUsers();

    /**
     * 根据ID查询用户
     */
    ResultVO getUserById(Long id);

    /**
     * 修改用户
     */
    ResultVO updateUser(User user);

    /**
     * 删除用户
     */
    ResultVO deleteUser(Long id);
}
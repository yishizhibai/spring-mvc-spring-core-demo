package com.spring.demo.service.user.impl;

import com.spring.demo.entity.ResultVO;
import com.spring.demo.entity.User;
import com.spring.demo.mapper.UserMapper;
import com.spring.demo.service.user.UserService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    // 构造函数注入UserMapper
    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public ResultVO addUser(User user) {
        user.setId(UUID.randomUUID().toString());
        user.setCreateTime(new Date()); // 调用User的setCreateTime
        int rows = userMapper.insertUser(user);
        return rows > 0 ? ResultVO.success("用户新增成功", user) : ResultVO.error("用户新增失败");
    }

    @Override
    public ResultVO listAllUsers() {
        List<User> userList = userMapper.selectAllUsers();
        return ResultVO.success("查询成功", userList);
    }

    @Override
    public ResultVO getUserById(String id) { // 匹配接口的String类型
        User user = userMapper.selectUserById(id);
        return user != null ? ResultVO.success("查询成功", user) : ResultVO.error("用户不存在");
    }

    @Override
    public ResultVO updateUser(User user) {
        int rows = userMapper.updateUser(user);
        return rows > 0 ? ResultVO.success("用户更新成功", user) : ResultVO.error("用户更新失败");
    }

    @Override
    public ResultVO deleteUser(String id) { // 匹配接口的String类型
        int rows = userMapper.deleteUserById(id);
        return rows > 0 ? ResultVO.success("用户删除成功", null) : ResultVO.error("用户删除失败");
    }
}
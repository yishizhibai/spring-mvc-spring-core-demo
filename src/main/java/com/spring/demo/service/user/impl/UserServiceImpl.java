package com.spring.demo.service.user.impl;

import com.spring.demo.entity.ResultVO;
import com.spring.demo.entity.User;
import com.spring.demo.repository.UserRepository;
import com.spring.demo.service.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;
    // 构造器注入
    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public ResultVO addUser(User user) {
        // 业务校验
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            log.error("新增用户失败：用户名不能为空");
            return ResultVO.error("用户名不能为空");
        }
        if (user.getAge() != null && (user.getAge() < 0 || user.getAge() > 150)) {
            log.error("新增用户失败：年龄不合法，年龄：{}", user.getAge());
            return ResultVO.error("年龄必须在0-150之间");
        }

        User savedUser = userRepository.save(user);
        log.info("新增用户成功：{}", savedUser.getUsername());
        return ResultVO.success("新增用户成功", savedUser);
    }

    @Override
    public ResultVO listAllUsers() {
        List<User> userList = userRepository.findAll();
        log.info("查询所有用户，数量：{}", userList.size());
        return ResultVO.success("查询成功",userList);
    }

    @Override
    public ResultVO getUserById(Long id) {
        if (id == null || id <= 0) {
            return ResultVO.error("用户ID不合法");
        }
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            return ResultVO.success("用户已存在",optionalUser.get());
        } else {
            log.error("查询用户失败：ID{}不存在", id);
            return ResultVO.error("用户不存在");
        }
    }

    @Override
    public ResultVO updateUser(User user) {
        if (user.getId() == null || user.getId() <= 0) {
            return ResultVO.error("用户ID不合法");
        }
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            return ResultVO.error("用户名不能为空");
        }

        boolean updateResult = userRepository.update(user);
        if (updateResult) {
            log.info("修改用户成功：ID{}", user.getId());
            return ResultVO.success("修改用户成功");
        } else {
            log.error("修改用户失败：ID{}不存在", user.getId());
            return ResultVO.error("用户不存在");
        }
    }

    @Override
    public ResultVO deleteUser(Long id) {
        if (id == null || id <= 0) {
            return ResultVO.error("用户ID不合法");
        }

        boolean deleteResult = userRepository.delete(id);
        if (deleteResult) {
            log.info("删除用户成功：ID{}", id);
            return ResultVO.success("删除用户成功");
        } else {
            log.error("删除用户失败：ID{}不存在", id);
            return ResultVO.error("用户不存在");
        }
    }
}
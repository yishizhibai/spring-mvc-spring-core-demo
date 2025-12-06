package com.spring.demo.controller;

import com.spring.demo.entity.ResultVO;
import com.spring.demo.entity.User;
import com.spring.demo.service.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    // 构造器注入
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 新增用户
     */
    @PostMapping("/add")
    public ResultVO addUser(@RequestBody User user) {
        return userService.addUser(user);
    }

    /**
     * 查询所有用户
     */
    @GetMapping("/list")
    public ResultVO listAllUsers() {
        return userService.listAllUsers();
    }

    /**
     * 根据ID查询用户
     */
    @GetMapping("/{id}")
    public ResultVO getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    /**
     * 修改用户
     */
    @PutMapping("/update")
    public ResultVO updateUser(@RequestBody User user) {
        return userService.updateUser(user);
    }

    /**
     * 删除用户
     */
    @DeleteMapping("/delete/{id}")
    public ResultVO deleteUser(@PathVariable Long id) {
        return userService.deleteUser(id);
    }
}
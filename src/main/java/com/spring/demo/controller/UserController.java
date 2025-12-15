package com.spring.demo.controller;

import com.spring.demo.entity.ResultVO;
import com.spring.demo.entity.User;
import com.spring.demo.service.user.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 新增用户
    @PostMapping("/add")
    public ResultVO addUser(@RequestBody User user) {
        return userService.addUser(user);
    }

    // 查询所有用户
    @GetMapping("/list")
    public ResultVO listAllUsers() {
        return userService.listAllUsers();
    }

    // 根据ID查询用户
    @GetMapping("/{id}")
    public ResultVO getUserById(@PathVariable String id) {
        return userService.getUserById(id);
    }

    // 更新用户
    @PutMapping("/update")
    public ResultVO updateUser(@RequestBody User user) {
        return userService.updateUser(user);
    }

    // 删除用户
    @DeleteMapping("/{id}")
    public ResultVO deleteUser(@PathVariable String id) {
        return userService.deleteUser(id);
    }
}
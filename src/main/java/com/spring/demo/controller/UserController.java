package com.spring.demo.controller;

import com.spring.demo.entity.PageRequest;
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

    // 原有接口保留
    @PostMapping("/add")
    public ResultVO addUser(@RequestBody User user) {
        return userService.addUser(user);
    }

    @GetMapping("/list")
    public ResultVO listAllUsers() {
        return userService.listAllUsers();
    }

    @GetMapping("/{id}")
    public ResultVO getUserById(@PathVariable String id) {
        return userService.getUserById(id);
    }

    @PutMapping("/update")
    public ResultVO updateUser(@RequestBody User user) {
        return userService.updateUser(user);
    }

    @DeleteMapping("/{id}")
    public ResultVO deleteUser(@PathVariable String id) {
        return userService.deleteUser(id);
    }

    @PostMapping("/page")
    public ResultVO listUserByPage(@RequestBody PageRequest pageRequest) {
        return userService.listUserByPage(pageRequest);
    }
    @GetMapping("/search")
    public ResultVO searchUserByUsername(@RequestParam(required = false, defaultValue = "") String usernameKeyword) {
        return userService.searchUserByUsername(usernameKeyword);
    }
}
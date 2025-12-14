package com.spring.demo.controller;

import com.spring.demo.aop.annotation.RateLimit; // 新增导入限流注解
import com.spring.demo.entity.ResultVO;
import com.spring.demo.entity.User;
import com.spring.demo.service.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 新增：添加限流注解，限制每秒3次请求
    @PostMapping("/add")
    @RateLimit(qps = 3, message = "用户新增接口调用频繁，请稍后再试")
    public ResultVO addUser(@RequestBody User user) {
        return userService.addUser(user);
    }

    // 新增：添加限流注解，限制每秒10次请求
    @GetMapping("/list")
    @RateLimit(qps = 10, message = "用户列表查询频繁，请稍后再试")
    public ResultVO listAllUsers(@RequestParam(required = false) String username) {
        ResultVO result = userService.listAllUsers();
        if (result.getCode() == 200 && username != null && !username.trim().isEmpty()) {
            List<User> userList = (List<User>) result.getData();
            List<User> filteredList = userList.stream()
                    .filter(user -> user.getUsername().contains(username.trim()))
                    .collect(Collectors.toList());
            return ResultVO.success("查询用户列表成功", filteredList);
        }
        return result;
    }

    // 新增：添加限流注解，限制每秒5次请求
    @GetMapping("/{id}")
    @RateLimit(qps = 5, message = "用户查询接口调用频繁，请稍后再试")
    public ResultVO getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    // 新增：添加限流注解，限制每秒3次请求
    @PutMapping("/update")
    @RateLimit(qps = 3, message = "用户编辑接口调用频繁，请稍后再试")
    public ResultVO updateUser(@RequestBody User user) {
        return userService.updateUser(user);
    }

    // 新增：添加限流注解，限制每秒2次请求
    @DeleteMapping("/delete/{id}")
    @RateLimit(qps = 2, message = "用户删除接口调用频繁，请稍后再试")
    public ResultVO deleteUser(@PathVariable Long id) {
        return userService.deleteUser(id);
    }
}
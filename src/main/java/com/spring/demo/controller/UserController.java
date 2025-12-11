package com.spring.demo.controller;

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

    @PostMapping("/add")
    public ResultVO addUser(@RequestBody User user) {
        return userService.addUser(user);
    }

    // 新增：支持用户名搜索
    @GetMapping("/list")
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

    @GetMapping("/{id}")
    public ResultVO getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PutMapping("/update")
    public ResultVO updateUser(@RequestBody User user) {
        return userService.updateUser(user);
    }

    @DeleteMapping("/delete/{id}")
    public ResultVO deleteUser(@PathVariable Long id) {
        return userService.deleteUser(id);
    }
}
package com.spring.demo.service.user.impl;

import com.spring.demo.entity.PageRequest;
import com.spring.demo.entity.PageResult;
import com.spring.demo.entity.ResultVO;
import com.spring.demo.entity.User;
import com.spring.demo.mapper.UserMapper;
import com.spring.demo.service.user.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    // 原有方法实现保留...
    @Override
    public ResultVO addUser(User user) {
        try {
            user.setId(UUID.randomUUID().toString().replace("-", ""));
            int rows = userMapper.insertUser(user);
            return rows > 0 ? ResultVO.success("用户新增成功", null) : ResultVO.error("用户新增失败");
        } catch (Exception e) {
            return ResultVO.error("用户新增异常：" + e.getMessage());
        }
    }

    @Override
    public ResultVO listAllUsers() {
        try {
            List<User> userList = userMapper.selectAllUser();
            return ResultVO.success("查询所有用户成功", userList);
        } catch (Exception e) {
            return ResultVO.error("查询所有用户异常：" + e.getMessage());
        }
    }

    @Override
    public ResultVO getUserById(String id) {
        try {
            User user = userMapper.selectUserById(id);
            if (user != null) {
                return ResultVO.success("查询用户成功", user);
            } else {
                return ResultVO.error("用户不存在");
            }
        } catch (Exception e) {
            return ResultVO.error("查询用户异常：" + e.getMessage());
        }
    }

    @Override
    public ResultVO updateUser(User user) {
        try {
            int rows = userMapper.updateUser(user);
            return rows > 0 ? ResultVO.success("用户修改成功", null) : ResultVO.error("用户修改失败");
        } catch (Exception e) {
            return ResultVO.error("用户修改异常：" + e.getMessage());
        }
    }

    @Override
    public ResultVO deleteUser(String id) {
        try {
            int rows = userMapper.deleteUserById(id);
            return rows > 0 ? ResultVO.success("用户删除成功", null) : ResultVO.error("用户删除失败");
        } catch (Exception e) {
            return ResultVO.error("用户删除异常：" + e.getMessage());
        }
    }
    // 新增：分页查询用户实现（返回ResultVO，封装分页结果）
    @Override
    public ResultVO listUserByPage(PageRequest pageRequest) {
        try {
            // 计算分页偏移量：offset = (pageNum - 1) * pageSize
            int offset = (pageRequest.getPageNum() - 1) * pageRequest.getPageSize();
            // 查询分页数据
            List<User> userList = userMapper.selectUserByPage(offset, pageRequest.getPageSize());
            // 查询总记录数
            long total = userMapper.selectUserTotal();
            // 封装分页结果
            PageResult<User> pageResult = new PageResult<>(pageRequest.getPageNum(), pageRequest.getPageSize(), total, userList);
            return ResultVO.success("分页查询用户成功", pageResult);
        } catch (Exception e) {
            return ResultVO.error("分页查询用户异常：" + e.getMessage());
        }
    }
    // 新增：按用户名搜索实现（最简单逻辑）
    @Override
    public ResultVO searchUserByUsername(String usernameKeyword) {
        try {
            // 若关键词为空，返回所有用户（可选逻辑，也可返回空）
            List<User> userList = userMapper.selectUserByUsername(usernameKeyword == null ? "" : usernameKeyword);
            return ResultVO.success("按用户名搜索成功", userList);
        } catch (Exception e) {
            return ResultVO.error("按用户名搜索异常：" + e.getMessage());
        }
    }
}
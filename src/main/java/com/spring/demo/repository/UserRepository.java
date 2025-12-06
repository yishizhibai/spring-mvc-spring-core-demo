package com.spring.demo.repository;

import com.spring.demo.entity.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class UserRepository {
    // 内存存储用户信息
    private final List<User> userList = new ArrayList<>();
    // 自增ID生成器
    private final AtomicLong idGenerator = new AtomicLong(1);

    /**
     * 新增用户
     */
    public User save(User user) {
        user.setId(idGenerator.getAndIncrement());
        userList.add(user);
        return user;
    }

    /**
     * 查询所有用户
     */
    public List<User> findAll() {
        return new ArrayList<>(userList);
    }

    /**
     * 根据ID查询用户
     */
    public Optional<User> findById(Long id) {
        return userList.stream().filter(u -> u.getId().equals(id)).findFirst();
    }

    /**
     * 修改用户
     */
    public boolean update(User user) {
        Optional<User> optionalUser = findById(user.getId());
        if (optionalUser.isPresent()) {
            User oldUser = optionalUser.get();
            oldUser.setUsername(user.getUsername());
            oldUser.setAge(user.getAge());
            oldUser.setPhone(user.getPhone());
            return true;
        }
        return false;
    }

    /**
     * 删除用户
     */
    public boolean delete(Long id) {
        return userList.removeIf(u -> u.getId().equals(id));
    }
}
package com.spring.demo.mapper;

import com.spring.demo.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {
    // 新增用户：补充create_time字段
    @Insert("INSERT INTO user(id, username, age, phone, create_time) VALUES(#{id}, #{username}, #{age}, #{phone}, #{createTime})")
    int insertUser(User user);

    // 根据ID查询用户
    @Select("SELECT * FROM user WHERE id = #{id}")
    User selectUserById(String id);

    // 查询所有用户
    @Select("SELECT * FROM user")
    List<User> selectAllUsers();

    // 更新用户
    @Update("UPDATE user SET username = #{username}, age = #{age}, phone = #{phone} WHERE id = #{id}")
    int updateUser(User user);

    // 删除用户
    @Delete("DELETE FROM user WHERE id = #{id}")
    int deleteUserById(String id);
}
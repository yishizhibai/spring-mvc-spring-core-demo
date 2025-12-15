package com.spring.demo.mapper;

import com.spring.demo.entity.User;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface UserMapper {
    // 原有方法保留
    @Insert("INSERT INTO user(id, username, age, phone) VALUES(#{id}, #{username}, #{age}, #{phone})")
    int insertUser(User user);

    @Update("UPDATE user SET username = #{username}, age = #{age}, phone = #{phone} WHERE id = #{id}")
    int updateUser(User user);

    @Select("SELECT id, username, age, phone FROM user")
    List<User> selectAllUser();

    @Select("SELECT id, username, age, phone FROM user WHERE id = #{id}")
    User selectUserById(String id);

    @Delete("DELETE FROM user WHERE id = #{id}")
    int deleteUserById(String id);

    @Select("SELECT COUNT(*) FROM user")
    long selectUserTotal();
    @Select("SELECT id, username, age, phone FROM user LIMIT #{offset}, #{pageSize}")
    List<User> selectUserByPage(int offset, int pageSize);

    @Select("SELECT id, username, age, phone, create_time AS createTime FROM user WHERE username LIKE CONCAT('%', #{usernameKeyword}, '%')")
    List<User> selectUserByUsername(String usernameKeyword);
}

package com.spring.demo;

import com.spring.demo.controller.UserController;
import com.spring.demo.entity.ResultVO;
import com.spring.demo.entity.User;
import com.spring.demo.service.user.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * UserController的单元测试（验证构造函数注入的可测试性）
 */
// 若需要启动Spring容器，添加@SpringBootTest；若仅测试逻辑，可省略（直接手动new）
@SpringBootTest
public class UserControllerTest {
    // Mock：模拟UserService的实现（无需真实的UserService实例）
    @Mock
    private UserService mockUserService;

    // 测试方法：验证用户新增接口的依赖注入与逻辑
    @Test
    public void testAddUser() {
        // 步骤1：手动注入Mock依赖（构造函数注入的优势：可直接new，无需Spring容器）
        UserController userController = new UserController(mockUserService);

        // 步骤2：模拟UserService的返回结果
        User testUser = new User();
        testUser.setUsername("测试用户");
        testUser.setAge(20);
        testUser.setPhone("13800138000");
        // 当调用mockUserService.addUser(testUser)时，返回成功的ResultVO
        Mockito.when(mockUserService.addUser(Mockito.any(User.class))).thenReturn(ResultVO.success("用户新增成功", testUser));

        // 步骤3：调用UserController的addUser方法
        ResultVO result = userController.addUser(testUser);

        // 步骤4：断言结果是否符合预期
        assertEquals(200, result.getCode()); // 假设ResultVO的code=200表示成功
        assertEquals("用户新增成功", result.getMsg());
        assertEquals(testUser, result.getData());
    }
}

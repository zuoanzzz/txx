package com.bosc.txx;

import com.bosc.txx.dao.UserMapper;
import com.bosc.txx.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.util.List;

/**
 * 快速验证
 *
 * @author zhoulei
 * @date 2025/8/25
 */

@SpringBootTest
public class SampleTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void testSelect() {
        System.out.println(("----- selectAll method test ------"));
        List<User> userList = userMapper.selectList(null);
        Assert.isTrue(1 == userList.size(), "");
        userList.forEach(System.out::println);
    }

}

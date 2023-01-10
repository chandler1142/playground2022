package com.example.mybatisentity;

import com.example.mybatisentity.entity.User;
import com.example.mybatisentity.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

//@SpringBootTest(classes = MybatisEntityApplication.class)
@org.mybatis.spring.boot.test.autoconfigure.MybatisTest
@Slf4j
public class MybatisTest {

    @Autowired
    UserMapper userMapper;

    @Test
    public void test() {
        User chandler = userMapper.selectOneUser("chandler");
        log.error(chandler.toString());
    }

}

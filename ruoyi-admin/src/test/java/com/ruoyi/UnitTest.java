package com.ruoyi;

import com.ruoyi.common.config.RuoYiConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@DisplayName("单元测试案例")
public class UnitTest {

    @Autowired
    private RuoYiConfig ruoYiConfig;


    @DisplayName("单元测试1")
    @Test
    public void test1() {
        System.out.println(ruoYiConfig);
    }
}

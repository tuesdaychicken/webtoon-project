package com.example.webtoon.health;

import com.example.webtoon.WebtoonApplication;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = WebtoonApplication.class)
@ActiveProfiles("test")
public class DbConnectivityTest {
    @Autowired
    JdbcTemplate jdbc;

    @Test
    @DisplayName("애플리케이션 컨텍스트 + Oracle 연결 확인")
    void contextAndDb() {
        Integer one = jdbc.queryForObject("SELECT 1 FROM DUAL", Integer.class);
        assertEquals(1, one);
    }
}

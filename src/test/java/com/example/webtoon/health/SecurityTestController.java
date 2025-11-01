package com.example.webtoon.health;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityTestController{

    @Autowired
    private MockMvc mockMvc;

    @Test // 테스트 메서드
    @DisplayName("GET /api/securityTest -> 200 OK and 'ok'")
    void health_shouldReturnOk() throws Exception {
        mockMvc.perform(get("/api/securityTest")) // /api/securityTest GET 요청
                .andExpect(status().isOk()) // 200 OK 기대
                .andExpect(content().string("ok"));
    }
}
package com.example.webtoon.securitytest;

import org.junit.jupiter.api.DisplayName; // 테스트 표기 import
import org.junit.jupiter.api.Test; // 테스트 어노테이션 import
import org.springframework.beans.factory.annotation.Autowired; // 의존성 주입 import
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc; // MockMvc 자동설정 import
import org.springframework.boot.test.context.SpringBootTest; // 통합 테스트 import
import org.springframework.test.web.servlet.MockMvc; // MockMvc import
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get; // GET 요청 빌더 import
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status; // 상태 검증 import
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content; // 본문 검증 import

@SpringBootTest
@AutoConfigureMockMvc
class SecurityTestControllerTest {

    @Autowired
    private MockMvc mockMvc; // HTTP 호출 흉내 객체

    @Test // 테스트 메서드
    @DisplayName("GET /api/securityTest -> 200 OK and 'ok'") // 테스트 설명
    void health_shouldReturnOk() throws Exception {
        mockMvc.perform(get("/api/securityTest")) // /api/securityTest GET 요청
                .andExpect(status().isOk()) // 200 OK 기대
                .andExpect(content().string("ok"));
    }
}
package com.example.webtoon.session;

import com.example.webtoon.session.dto.LoginRequest;
import com.example.webtoon.user.UserRepository;
import com.example.webtoon.user.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@Transactional
public class SessionControllerTest {

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper om;
    @Autowired UserService userService;
    @Autowired UserRepository userRepository;

    @DisplayName("가입 요청, 성공시 204 응답 그리고 조회해서 맞는지 확인 테스트")
    @Test
    void login_success_then_me_true() throws Exception {

        // Given
        userService.register("loginTest", "로그인테스트", "pw123456", "a@ex.com", "로그인테스트");

        LoginRequest req = new LoginRequest();
        req.setUsername("loginTest");
        req.setPassword("pw123456");

        // When 로그인 시도
        MvcResult result = mvc.perform(post("/api/session")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isNoContent())
                .andReturn();

        // Then 같은 세션으로 조회
        MockHttpSession session = (MockHttpSession) result.getRequest().getSession(false);
        assertThat(session).isNotNull();

        mvc.perform(get("/api/session").session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.exists").value(true))
                .andExpect(jsonPath("$.username").value("loginTest"));
    }

    @DisplayName("가입 후 해당 아이디로 로그인 요청시 비밀번호 틀리면 로그인 실패 401 응답 INVALID_CREDENTIALS + 같은 세션으로 me는 exists:false")
    @Test
    void login_wrong_password_then_me_false() throws Exception {
        // Given
        userService.register("sessionTest", "세션테스트", "pw123456", "a@ex.com", "세션테스트");

        LoginRequest req = new LoginRequest();
        req.setUsername("sessionTest");
        req.setPassword("wrongpw123"); // 8자 이상이지만 틀리 비밀번호 값

        // When 로그인 실패
        MvcResult result = mvc.perform(post("/api/session")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value("INVALID_CREDENTIALS"))
                .andReturn();

        // Then 같은 세션으로 me 조회 → exists:false
        MockHttpSession session = (MockHttpSession) result.getRequest().getSession(false);
        // 컨트롤러에서 HttpSession 파라미터 사용으로 세션 자체는 생성될 수 있으나, 로그인 속성은 없음 .andExpect(jsonPath("$.exists").value(false));
        mvc.perform(get("/api/session").session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.exists").value(false));
    }

    @DisplayName("비밀번호 8자리 이하일 경우 400 응답")
    @Test
    void login_validation_error_short_password() throws Exception {
        // Given
        LoginRequest req = new LoginRequest();
        req.setUsername("anyone");
        req.setPassword("12"); // 비밀번호 8자리 미만

        // When & Then
        mvc.perform(post("/api/session")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"));
    }

    @DisplayName("로그인 하지 않은 사용자 Get 요청시 200 응답이지만 세션은 존재하지 않음")
    @Test
    void me_without_session() throws Exception {
        // When & Then
        mvc.perform(get("/api/session"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.exists").value(false));
    }

    @DisplayName("로그인 완료 후 로그아웃 세션 삭제 204 응답")
    @Test
    void logout_then_me_false() throws Exception {
        // Given
        userService.register("logoutTest", "로그아웃테스트", "pw123456", "a@ex.com", "로그아웃테스트");

        LoginRequest req = new LoginRequest();
        req.setUsername("logoutTest");
        req.setPassword("pw123456");

        MvcResult login = mvc.perform(post("/api/session")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isNoContent())
                .andReturn();

        MockHttpSession session = (MockHttpSession) login.getRequest().getSession(false);
        assertThat(session).isNotNull();

        // When 로그아웃
        mvc.perform(delete("/api/session").session(session))
                .andExpect(status().isNoContent());

        // Then 로그아웃 후 세션 확인 Get 요청
        mvc.perform(get("/api/session").session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.exists").value(false));
    }
}

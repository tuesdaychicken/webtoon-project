package com.example.webtoon.user;

import com.example.webtoon.user.dto.ChangePasswordRequest;

import com.example.webtoon.user.dto.CreateUserRequest;
import com.example.webtoon.user.dto.UpdateUserRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@Transactional
class UserControllerTest {

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper om;
    @Autowired UserService userService;
    @Autowired UserRepository userRepository;

    @DisplayName("사용자 생성 테스트, 201 응답")
    @Test
    void create_user_save_db() throws Exception {
        // Given
        CreateUserRequest req = new CreateUserRequest();
        req.setUsername("createA");
        req.setName("createA");
        req.setPassword("pw123456");
        req.setEmail("a@ex.com");
        req.setNickname("생성테스트");

        // When & Then (상태코드 + Location 헤더 확인)
        mvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/users/createA"));

        // Then
        User saved = userRepository.findByUsername("createA").orElseThrow();
        assertThat(saved.getName()).isEqualTo("createA");
    }

    @DisplayName("사용자 중복일 경우 409 응답")
    @Test
    void create_user_duplication() throws Exception {
        // Given
        userService.register("중복", "중복 저장", "pw123456", "a@ex.com", "중복");
        CreateUserRequest req = new CreateUserRequest();
        req.setUsername("중복");
        req.setName("중복 생성");
        req.setPassword("pw123456");
        req.setEmail("a@ex.com");
        req.setNickname("중복");

        // When & Then
        mvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isConflict());
    }

    @DisplayName("사용자 조회 성공시 응답 200")
    @Test
    void get_user_data_test() throws Exception {
        // Given
        userService.register("check", "조회", "pw123456", "a@ex.com", "조회");

        // When & Then
        mvc.perform(get("/api/users/{username}", "check"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("check"))
                .andExpect(jsonPath("$.name").value("조회"))
                .andExpect(jsonPath("$.email").value("a@ex.com"))
                .andExpect(jsonPath("$.nickname").value("조회"));
    }

    @DisplayName("없는 사용자 조회 404 응답")
    @Test
    void getUser_404() throws Exception {

        // When & Then
        mvc.perform(get("/api/users/{username}", "nix"))
                .andExpect(status().isNotFound());
    }

    @DisplayName("기존 사용자 수정 요청시 204 응답 + DB 값이 바뀐다")
    @Test
    void update_user_change_db() throws Exception {
        // Given
        userService.register("updateA", "updateA", "pw123456", "a@ex.com", "수정전");
        UpdateUserRequest req = new UpdateUserRequest();
        req.setName("updateB"); req.setEmail("test@new.com"); req.setNickname("수정후");

        // When & Then (상태코드 확인)
        mvc.perform(patch("/api/users/{username}", "updateA")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isNoContent());

        // Then (DB 값 확인)
        User updated = userRepository.findByUsername("updateA").orElseThrow();
        assertThat(updated.getName()).isEqualTo("updateB");
        assertThat(updated.getEmail()).isEqualTo("test@new.com");
        assertThat(updated.getNickname()).isEqualTo("수정후");
    }

    @DisplayName("기존 사용자가 비번 변경할 경우 204 응답 + 비번 해시가 바뀐다")
    @Test
    void change_password_and_hashChanged() throws Exception {
        // Given
        userService.register("pwUpdate", "pwUpdateName", "oldPw1234", "a@ex.com", "비번컨트롤테스트");
        String beforeHash = userRepository.findByUsername("pwUpdate").orElseThrow().getPassword();
        ChangePasswordRequest req = new ChangePasswordRequest();
        req.setNewPassword("newPw1234");

        // When & Then
        mvc.perform(patch("/api/users/{username}/password", "pwUpdate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isNoContent());

        // Then
        String afterHash = userRepository.findByUsername("pwUpdate").orElseThrow().getPassword();
        assertThat(afterHash).isNotEqualTo(beforeHash);
    }

    @DisplayName("기존 사용자를 삭제, DELETE 요청하면 204 응답 + DB에서 사라진다")
    @Test
    void delete_user_removed_db() throws Exception {
        // Given
        userService.register("deleteA", "삭제", "pw123456", "a@ex.com", "삭제A");
        assertThat(userRepository.existsByUsername("deleteA")).isTrue();

        // When & Then
        mvc.perform(delete("/api/users/{username}", "deleteA"))
                .andExpect(status().isNoContent());

        // Then
        assertThat(userRepository.existsByUsername("deleteA")).isFalse();
    }

    @DisplayName("사용자 수정시 없을 경우 404오류 발생해야한다")
    @Test
    void update_user_404_test() throws Exception {
        UpdateUserRequest req = new UpdateUserRequest();
        req.setName("test");

        mvc.perform(patch("/api/users/{username}", "nix")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isNotFound());
    }

    @DisplayName("사용자 삭제시 없을 경우 404 오류")
    @Test
    void delete_user_404_test() throws Exception {
        mvc.perform(delete("/api/users/{username}", "nix"))
                .andExpect(status().isNotFound());
    }
}

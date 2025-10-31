package com.example.webtoon.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@Import({com.example.webtoon.support.PasswordEncoderTestConfig.class, UserService.class})
class UserServiceTest {

    @Autowired UserRepository userRepository;
    @Autowired UserService userService;

    @Test
    @DisplayName("회원가입 시 name 중복이면 예외가 발생한다")
    void register_duplication_trow_username() {
        // given
        userRepository.save(
                User.builder()
                        .username("serviceTest")
                        .name("serviceTest")
                        .password("encoded")    // 테스트용
                        .email("a@ex.com")
                        .nickname("테스트")
                        .build());

        // when / then
        assertThatThrownBy(() ->
                userService.register("serviceTest", "test", "pass1234", "a2@ex.com", "테스트"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이미 사용 중인 username");
    }

    @Test
    @DisplayName("회원가입 시 비밀번호는 해시되어 저장된다")
    void register_encodes_password() {
        // when
        Long id = userService.register("testService1", "test","pw", "a@ex.com", "테스트1");

        // then
        User saved = userRepository.findById(id).orElseThrow();
        assertThat(saved.getPassword()).isNotEqualTo("plain-pass");
        assertThat(saved.getPassword()).startsWith("$2a")
                .as("BCrypt 포맷이어야 함");
    }

    @Test
    @DisplayName("가입 후 아이디로 조회하면 동일 사용자를 얻는다")
    void findByUsername_after_register() {
        // given
        Long id = userService.register("me", "test", "pw", "a@ex.com", "테스트2");

        // when
        User found = userService.findByUsername("me").orElseThrow();

        // then
        assertThat(found.getId()).isEqualTo(id);
        assertThat(found.getEmail()).isEqualTo("a@ex.com");
        assertThat(found.getNickname()).isEqualTo("테스트2");
    }
}

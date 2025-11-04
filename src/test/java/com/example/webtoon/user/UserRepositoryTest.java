package com.example.webtoon.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {

    @Autowired
    UserRepository repo;

    @Test
    @DisplayName("사용자 저장 후 조회")
    void save_and_find() {
        User saved = repo.save(
                User.builder()
                        .username("testRepo")
                        .name("황규성")
                        .nickname("테스트다")
                        .email("test@example.com")
                        .password("raw-password")
                        .build()
        );

        Optional<User> found = repo.findByUsername("testRepo");

        assertThat(found).isPresent();
        assertThat(found.get().getId()).isNotNull();        // PK가 발급되었는지
        assertThat(found.get().getName()).isEqualTo("황규성");
        assertThat(found.get().getNickname()).isEqualTo("테스트다");
        assertThat(found.get().getEmail()).isEqualTo("test@example.com");
    }
}

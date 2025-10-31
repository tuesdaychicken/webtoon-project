package com.example.webtoon.user;

import com.example.webtoon.WebtoonApplication;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // 실제 Oracle 사용
class UserRepositoryTest {

    @Autowired
    UserRepository repo;

    @Test
    @DisplayName("USERS: 저장 후 조회")
    void saveAndFind() {
        User u = new User();
        u.setName("repoTest");
        u.setEmail("repoTest@example.com");

        User saved = repo.save(u);
        assertNotNull(saved.getId());

        User found = repo.findById(saved.getId()).orElseThrow();
        assertEquals("repoTest", found.getName());
        assertEquals("repoTest@example.com", found.getEmail());
        assertNotNull(found.getCreatedAt());
    }
}

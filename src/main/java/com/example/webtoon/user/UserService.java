package com.example.webtoon.user;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public Long register(String username, String name, String rawPassword, String email, String nickname) {
        // username 중복 검증
        if (userRepository.findByUsername(username).isPresent()) {         // 이미 존재하면
            throw new IllegalArgumentException("이미 사용 중인 username 입니다: " + username);
        }

        // 비밀번호 암호화
        String encoded = passwordEncoder.encode(rawPassword);      // BCrypt 해시 생성

        // 엔티티 생성 및 저장
        User user = User.builder()
                .username(username) // 유니크 username
                .name(name)
                .password(encoded)  // 해시 저장 (원문 금지)
                .email(email)       // 이메일
                .nickname(nickname) // 닉네임
                .build();

        User saved = userRepository.save(user);
        return saved.getId();   // 발급된 PK 반환
    }

    @Transactional(readOnly = true)
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}

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

    /**
     * 유저 생성
     * @param username
     * @param name
     * @param rawPassword
     * @param email
     * @param nickname
     * @return 유저 기본키 seq id값(Long)
     */
    @Transactional
    public Long register(String username, String name, String rawPassword, String email, String nickname) {

        // username 중복 검증
        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("이미 사용 중인 username 입니다: " + username);
        }

        // 비밀번호 암호화
        String encoded = passwordEncoder.encode(rawPassword);      // BCrypt 해시 생성

        // 엔티티 생성 및 저장
        User user = User.builder()
                .username(username) // 로그인 아이디
                .name(name)         // 이름
                .password(encoded)  // 비밀번호 해시
                .email(email)       // 이메일
                .nickname(nickname) // 닉네임
                .build();

        return userRepository.save(user).getId(); // 발급된 PK 반환
    }

    /**
     * 유저 한명 조회
     * @param username
     * @return 유저 반환
     */
    @Transactional(readOnly = true)
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * 유저 수정 (이름, 이메일, 닉네임)
     * @param username
     * @param name
     * @param email
     * @param nickname
     */
    @Transactional
    public void updateUser(String username, String name, String email, String nickname) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다: " + username));
        user.updateProfile(name, email, nickname); // JPA 변경 감지하여 반영
    }

    /**
     * 유저 비밀번호 변경
     * @param username
     * @param rawPassword
     */
    @Transactional
    public void changePassword(String username, String rawPassword) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다: " + username));
        String encoded = passwordEncoder.encode(rawPassword);
        user.changePassword(encoded); // JPA 변경 감지하여 반영
    }

    /**
     * 유저 삭제
     * @param username
     */
    @Transactional
    public void deleteByUsername(String username) {
        if (!userRepository.existsByUsername(username)) {
            throw new RuntimeException("사용자를 찾을 수 없습니다: " + username);
        }
        userRepository.deleteByUsername(username);
    }
}

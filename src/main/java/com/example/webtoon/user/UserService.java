package com.example.webtoon.user;

import com.example.webtoon.user.exception.UserNotFoundException;
import com.example.webtoon.user.exception.UsernameAlreadyExistsException;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * User 비즈니스 로직
 * 생성, 조회, 수정, 삭제
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 유저 생성, 회원 가입
     * @param username
     * @param name
     * @param rawPassword
     * @param email
     * @param nickname
     * @return 유저 기본키 seq id값(Long)
     * 유저 중복 409 예외
     */
    @Transactional
    public Long register(String username, String name, String rawPassword, String email, String nickname) {

        // username 중복 검증
        if (userRepository.existsByUsername(username)) {
            // 예외 처리, 유저 중복이면 409 예외
            throw new UsernameAlreadyExistsException(username);
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
     * 유저 일부 수정 (이름, 이메일, 닉네임)
     * @param username
     * @param name
     * @param email
     * @param nickname
     * 유저 없음 404 예외
     */
    @Transactional
    public void updateUser(String username, String name, String email, String nickname) {
        User user = userRepository.findByUsername(username)
                // 예외 처리, 유저 없으면 404로 매핑될 예외
                .orElseThrow(() -> new UserNotFoundException(username));
        user.updateProfile(name, email, nickname); // JPA 변경 감지하여 반영
    }

    /**
     * 유저 비밀번호 변경
     * @param username
     * @param rawPassword
     * 예외 처리, pw 바꿀 유저 없는 경우 404
     */
    @Transactional
    public void changePassword(String username, String rawPassword) {
        User user = userRepository.findByUsername(username)
                // 예외 처리, 유저 없으면 404
                .orElseThrow(() -> new UserNotFoundException(username));
        String encoded = passwordEncoder.encode(rawPassword);
        user.changePassword(encoded); // JPA 변경 감지하여 반영
    }

    /**
     * 유저 삭제
     * @param username
     * 예외 처리, 삭제할 유저 없으면 404
     */
    @Transactional
    public void deleteByUsername(String username) {
        if (!userRepository.existsByUsername(username)) {
            //예외 처리, 유저 없으면 404
            throw new UserNotFoundException(username);
        }
        userRepository.deleteByUsername(username);
    }
}

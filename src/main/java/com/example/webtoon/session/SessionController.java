package com.example.webtoon.session;

import com.example.webtoon.common.ErrorResponse;
import com.example.webtoon.session.dto.LoginRequest;
import com.example.webtoon.session.dto.SessionMeResponse;
import com.example.webtoon.user.User;
import com.example.webtoon.user.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/session")
public class SessionController {

    // 세션키, 문자열 username 저장
    private static final String LOGIN_USER = "LOGIN_USER";

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    /**
     * 세션 생성 요청, 로그인
     * @param req
     * @param session
     * @return
     */
    @PostMapping
    public ResponseEntity<?> createSession(@RequestBody @Valid LoginRequest req, HttpSession session) {

        return userService.authenticate(req.getUsername(), req.getPassword())

                // 성공시 세션에 저장, 204
                .map(user -> {
                    session.setAttribute(LOGIN_USER, user.getUsername());
                    return ResponseEntity.noContent().build();
                })

                // 실패시 401
                .orElseGet(() -> ResponseEntity.status(401).body(
                                ErrorResponse.of("INVALID_CREDENTIALS", "아이디 또는 비밀번호가 올바르지 않습니다.")
                        )
                );
    }

    /**
     * 세션 조회 요청, 로그온
     * @param session
     * @return
     */
    @GetMapping
    public ResponseEntity<SessionMeResponse> getSession(HttpSession session) {
        // 세션에 로그인 사용자 키가 없으면 → 401 (본문 없음)
        Object attr = session.getAttribute(LOGIN_USER);
        if (attr == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .header(HttpHeaders.CACHE_CONTROL, "no-store")
                    .build(); // body 없음
        }

        // 세션엔 있으나 DB에 사용자가 없으면 → 401
        String username = (String) attr;
        return userService.findByUsername(username)
                .map(u -> ResponseEntity.ok()
                        .header(HttpHeaders.CACHE_CONTROL, "no-store")
                        .body(SessionMeResponse.of(
                                u.getUsername(),
                                u.getName(),
                                u.getNickname()
                        )))
                .orElseGet(() -> ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .header(HttpHeaders.CACHE_CONTROL, "no-store")
                        .build());
    }


    /**
     * 세션 삭제, 로그아웃
     * @param session
     * @return
     */
    @DeleteMapping
    public ResponseEntity<Void> deleteSession(HttpSession session) {

        session.invalidate();

        return ResponseEntity.noContent().build();
    }
}

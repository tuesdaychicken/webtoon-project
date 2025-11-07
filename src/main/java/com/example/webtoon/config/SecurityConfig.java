package com.example.webtoon.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

//시큐리티 설정 클래스
@Configuration
public class SecurityConfig {

    @Bean // 빈으로 등록
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable()) // CSRF 비활성화
                .headers(headers -> headers.frameOptions(frame -> frame.disable())) //H2 콘솔은 iframe으로 열림 그래서 비활성화 해야함
                .authorizeHttpRequests(auth -> auth // 요청 인가 규칙 부분
                        //홈과 파비콘, 로그인 등 기본 페이지
                        .requestMatchers("/", "/favicon.ico",
                                "/index.html", "/main.html","/login.html","/signup.html").permitAll()

                        // 정적 리소스 허용
                        .requestMatchers("/static/**", "/css/**", "/js/**", "/images/**").permitAll()

                        // 회원가입 + 세션 API
                        .requestMatchers("/api/users","/api/session").permitAll()

                        // 디비 테스트 경로
                        .requestMatchers("/api/dbcheck").permitAll()
                        // 시큐리티 테스트 경로
                        .requestMatchers("/api/securityTest").permitAll()

                        .anyRequest().permitAll() // 나머지는 전부 일단 허가 화면 연동 후 인증으로 전환 예저어어엉
                )
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();                 // BCrypt로 비밀번호 암호화
    }
}

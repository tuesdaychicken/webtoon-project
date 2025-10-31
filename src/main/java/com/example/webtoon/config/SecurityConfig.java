package com.example.webtoon.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
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
                        //홈과 파비콘 허용
                        .requestMatchers("/", "/favicon.ico").permitAll()

                        // 정적 리소스 허용
                        .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()

                        //디비 테스트 경로
                        .requestMatchers("/api/dbcheck").permitAll()

                        // securityTest는 모두 허용으로 일단 테스트 부터
                        .requestMatchers("/api/securityTest").permitAll()
                        .anyRequest().authenticated() // 나머지는 전부 인증 필요
                )
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}

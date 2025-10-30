package com.example.webtoon.config;

import org.springframework.context.annotation.Bean; // Bean 선언 import
import org.springframework.context.annotation.Configuration; // 설정 클래스 import
import org.springframework.security.config.Customizer; // 기본 설정용 import
import org.springframework.security.config.annotation.web.builders.HttpSecurity; // HttpSecurity import
import org.springframework.security.web.SecurityFilterChain; // 필터체인 import

//시큐리티 설정 클래스
@Configuration
public class SecurityConfig {

    @Bean // 빈으로 등록
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable()) // CSRF 비활성화
                .authorizeHttpRequests(auth -> auth // 요청 인가 규칙 부분
                        .requestMatchers("/api/securityTest").permitAll() // securityTest는 모두 허용으로 일단 테스트 부터
                        .anyRequest().authenticated() // 나머지는 전부 인증 필요
                )
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}

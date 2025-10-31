package com.example.webtoon.health;

import org.springframework.http.ResponseEntity; // 응답 객체 import
import org.springframework.web.bind.annotation.GetMapping; // GET 매핑 import
import org.springframework.web.bind.annotation.RequestMapping; // 공통 경로 import
import org.springframework.web.bind.annotation.RestController; // REST 컨트롤러 import

@RestController
@RequestMapping("/api")
public class SecurityTestController {

    @GetMapping("/securityTest") // GET /api/securityTest
    public ResponseEntity<String> securityTest() { // 문자열 본문으로 응답
        return ResponseEntity.ok("ok"); // 200 OK + 본문 "ok"
    }
}
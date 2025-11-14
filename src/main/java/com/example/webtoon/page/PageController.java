package com.example.webtoon.page;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 순수 페이지 라우팅만 하는 컨트롤러
 * 타임리프 뷰리졸브
 * Prefix (경로 접두사): src/main/resources/templates/
 * Suffix (경로 접미사): .html
 * Prefix + 경로명 + Suffix = 해당 파일 절대경로
 */
@Controller
public class PageController { // Modified this session

    /**
     * 메인 페이지 라우팅
     * @return "main" (/templates/main.html)
     */
    @GetMapping({"/", "/main"})
    public String main() {
        return "main";
    }

    /**
     * 로그인 페이지 라우팅
     * @return "login" (/templates/login.html)
     */
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    /**
     * 회원가입 페이지 라우팅
     * @return "signup" (/templates/signup.html)
     */
    @GetMapping("/signup")
    public String signup() {
        return "signup";
    }
}

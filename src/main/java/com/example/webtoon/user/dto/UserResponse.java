package com.example.webtoon.user.dto;

import com.example.webtoon.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class UserResponse {

    private String username;
    private String name;
    private String email;
    private String nickname;
    private LocalDateTime createdAt;

    // 정적 팩토리 메서드, 매핑 메서드
    public static UserResponse from(User u) {
        UserResponse r = new UserResponse();
        r.username = u.getUsername();
        r.name = u.getName();
        r.email = u.getEmail();
        r.nickname = u.getNickname();
        r.createdAt = u.getCreatedAt();
        return r;
    }

}

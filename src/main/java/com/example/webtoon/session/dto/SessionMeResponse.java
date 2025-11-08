package com.example.webtoon.session.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// 세션 조회 응답 DTO
@Getter
@NoArgsConstructor
public class SessionMeResponse {

    private boolean exists;
    private String username;
    private String name;
    private String nickname;

    public static SessionMeResponse none() { 
        SessionMeResponse r = new SessionMeResponse();
        r.exists = false;
        return r;
    }

    public static SessionMeResponse of(String username, String name, String nickname) { 
        SessionMeResponse r = new SessionMeResponse();
        r.exists = true;
        r.username = username;
        r.name = name;
        r.nickname = nickname;
        return r;
    }

}

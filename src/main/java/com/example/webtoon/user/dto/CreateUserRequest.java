package com.example.webtoon.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateUserRequest {

    private String username;
    private String name;
    private String password;
    private String email;
    private String nickname;
}

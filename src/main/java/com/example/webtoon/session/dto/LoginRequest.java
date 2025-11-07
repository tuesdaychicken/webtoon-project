package com.example.webtoon.session.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// 세션 생성 DTO
@Getter
@Setter
@NoArgsConstructor
public class LoginRequest {

    //로그인에 필요한 정보
    @NotBlank(message = "username은 필수입니다.")
    private String username;

    @NotBlank(message = "password는 필수입니다.")
    @Size(min = 8, message = "password는 8자 이상이어야 합니다.")
    private String password;
}

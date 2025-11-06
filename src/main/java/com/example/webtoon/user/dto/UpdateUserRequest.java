package com.example.webtoon.user.dto;

import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 유저 수정 요청 DTO
 * 뷰단에서 넘어온 json을 DTO에 담아서 서비스로
 */
@Getter
@Setter
@NoArgsConstructor
public class UpdateUserRequest {

    private String name;

    @Email(message = "email 형식이 올바르지 않습니다.")
    private String email;

    private String nickname;
}

package com.example.webtoon.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 유저 비밀번호 변경 요청 DTO
 * 뷰단에서 비밀번호 변경 요청이 오면 dto에 담아서 서비스로
 */
@Getter
@Setter
@NoArgsConstructor
public class ChangePasswordRequest {

    private String newPassword;

}

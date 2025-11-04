package com.example.webtoon.user.dto;

/**
 * 유저 비밀번호 변경 요청 DTO
 * 뷰단에서 비밀번호 변경 요청이 오면 dto에 담아서 서비스로
 */
public class ChangePasswordRequest {

    private String newPassword;

    public ChangePasswordRequest() {}

    public String getNewPassword() { return newPassword; }
    public void setNewPassword(String newPassword) { this.newPassword = newPassword; }
}

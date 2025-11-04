package com.example.webtoon.user.dto;


/**
 * 유저 수정 요청 DTO
 * 뷰단에서 넘어온 json을 DTO에 담아서 서비스로
 */
public class UpdateUserRequest {
    private String name;
    private String email;
    private String nickname;

    public UpdateUserRequest() {}

    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getNickname() { return nickname; }

    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setNickname(String nickname) { this.nickname = nickname; }
}

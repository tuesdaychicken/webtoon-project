package com.example.webtoon.user;

import jakarta.persistence.*;
import lombok.*;


import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "USERS")
@SequenceGenerator(
        name = "user_seq",
        sequenceName = "USER_SEQ",
        allocationSize = 1
)
public class User {

    //시퀀스
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "user_seq")
    @Column(name = "ID")
    private Long id;

    //로그인 아이디
    @Setter
    @Column(name = "USERNAME",
            unique = true,
            updatable = false,
            nullable = false,
            length = 50)
    private String username;

    //로그인 비민번호
    @Setter
    @Column(name = "PASSWORD",
            length = 100)
    private String password;

    //별명
    @Setter
    @Column(name = "NICANAME",
            unique = true,
            nullable = false,
            length = 30)
    private String nickname;

    //실제이름
    @Setter
    @Column(name = "NAME",
            nullable = false,
            length = 20)
    private String name;

    //아이디 되찾을 때 필요한 이메일 주소 기입은 선택
    @Setter
    @Column(name = "EMAIL",
            length = 50)
    private String email;

    //생성날짜
    @Column(name = "CREATED_AT",
            nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    void onCreate() {
        // 테스트용 DB 기본값 대신 코드로 세팅
        if (createdAt == null) createdAt = LocalDateTime.now();
    }

    // 유저 정보 수정(비밀번호 제외)
    public void updateProfile(String name, String email, String nickname) {
        if (name != null) this.name = name;
        if (email != null) this.email = email;
        if (nickname != null) this.nickname = nickname;
    }

    // 비밀번호 변경
    public void changePassword(String encodedPassword) {
        this.password = Objects.requireNonNull(encodedPassword, "encodedPassword");
    }

}

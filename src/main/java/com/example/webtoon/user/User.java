package com.example.webtoon.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "USERS")
@SequenceGenerator(
        name = "user_seq",
        sequenceName = "USER_SEQ",
        allocationSize = 1
)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
    private Long id;

    @Setter
    @Column(name = "NAME", nullable = false, length = 50)
    private String name;

    @Setter
    @Column(name = "EMAIL", length = 50)
    private String email;

    @Column(name = "CREATED_AT", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    void onCreate() {
        // 테스트용 DB 기본값 대신 코드로 세팅
        if (createdAt == null) createdAt = LocalDateTime.now();
    }

}

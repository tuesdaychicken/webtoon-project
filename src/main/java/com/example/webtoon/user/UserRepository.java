package com.example.webtoon.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // 유저이름으로 유저 정보 조회
    Optional<User> findByUsername(String username);

    // 유저이름으로 유저 존재유무 불린값으로 조회
    boolean existsByUsername(String username);

    // 유저이름으로 유저 삭제
    void deleteByUsername(String username);
}

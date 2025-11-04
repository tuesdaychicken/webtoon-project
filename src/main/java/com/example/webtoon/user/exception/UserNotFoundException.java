package com.example.webtoon.user.exception;

public class UserNotFoundException extends RuntimeException {

    /**
     * 유저네임으로 검색시 해당 유저 없음
     * 사용자를 찾을 수 없을 때 던지는 예외, 404(Not Found)
     * @param username
     */
    public UserNotFoundException(String username) {
        super("User not found: " + username);
    }
}

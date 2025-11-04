package com.example.webtoon.user.exception;

public class UsernameAlreadyExistsException extends RuntimeException {

    /**
     * 유저네임으로 검색시 이미 있음
     * 사용자 유저이름이 중복일 경우 던지는 예외, 409(Conflict)
     * @param username
     */
    public UsernameAlreadyExistsException(String username) {
        super("Username already exists: " + username);
    }
}

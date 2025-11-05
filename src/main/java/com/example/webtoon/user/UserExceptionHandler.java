package com.example.webtoon.user;

import com.example.webtoon.user.dto.ErrorResponse;

import com.example.webtoon.user.exception.UserNotFoundException;
import com.example.webtoon.user.exception.UsernameAlreadyExistsException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 사용자 관련 예외, HTTP 상태코드 매핑
 * @ExceptionHandler 로 연결
 */
@RestControllerAdvice
public class UserExceptionHandler {

    // 사용자 없음 404
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(UserNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ErrorResponse.of("USER_NOT_FOUND", e.getMessage()));
    }

    // username 중복 409
    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleConflict(UsernameAlreadyExistsException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ErrorResponse.of("USERNAME_EXISTS", e.getMessage()));
    }

    // 잘못된 요청 400
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(IllegalArgumentException e) {
        return ResponseEntity.badRequest()
                .body(ErrorResponse.of("BAD_BAD_REQUEST", e.getMessage()));
    }
}

package com.example.webtoon.common;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 실패에 대한 에러 응답 공통 포맷!
 * code : 에어 구분용 코드
 * message : 에러에 대한 설명
 * timestamp: 에러 발생 시간
 */
@NoArgsConstructor
@Getter
public class ErrorResponse {

    private String code;
    private String message;
    private LocalDateTime timestamp;

    public static ErrorResponse of(String code, String message) {
        ErrorResponse r = new ErrorResponse();
        r.code = code;
        r.message = message;
        r.timestamp = LocalDateTime.now();
        return r;
    }

}

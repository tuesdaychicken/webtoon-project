package com.example.webtoon.user.dto;

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
    private Map<String, String> errors; // 이건 아직 대기 (필드별 오류가 있을 때 라는데 아직)

    public static ErrorResponse of(String code, String message) {
        ErrorResponse r = new ErrorResponse();
        r.code = code;
        r.message = message;
        r.timestamp = LocalDateTime.now();
        return r;
    }

    // 이건 아직 대기
    public static ErrorResponse of(String code, String message, Map<String, String> errors) {
        ErrorResponse r = of(code, message);
        r.errors = errors;
        return r;
    }
}

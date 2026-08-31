package com.spotpulse.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 404 - 존재하지 않는 리소스 (Optional.orElseThrow() 기본 예외 포함)
    @ExceptionHandler({NotFoundException.class, NoSuchElementException.class})
    public ResponseEntity<Map<String, Object>> handleNotFound(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(Map.of("code", 404, "message", e.getMessage() != null ? e.getMessage() : "해당 리소스를 찾을 수 없습니다."));
    }

    // 400 - 잘못된 요청
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Map<String, Object>> handleBadRequest(BadRequestException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(Map.of("code", 400, "message", e.getMessage()));
    }

    // 500 - 그 외 모든 예외 (최종 안전망)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneral(Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(Map.of("code", 500, "message", "서버 내부 오류가 발생했습니다."));
    }
}
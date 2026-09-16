package com.mungdori.fallserver.adapter.exception;

import org.springframework.http.HttpStatus;

public class AuthException extends RuntimeException {
    private final HttpStatus status;

    public AuthException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() { return status; }

    public static AuthException unauthorized() {
        return new AuthException(HttpStatus.UNAUTHORIZED, "로그인 정보 또는 토큰이 올바르지 않습니다.");
    }
}

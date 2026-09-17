package com.mungdori.fallserver.domain.admin;

public record AdminRegisterRequest(
        String name,
        String loginId,
        String password,
        String code
) {
}

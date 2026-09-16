package com.mungdori.fallserver.adapter.security;

import com.mungdori.fallserver.adapter.exception.AuthException;
import com.mungdori.fallserver.domain.auth.Role;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
@RequiredArgsConstructor
public class AdminOnlyAspect {
    private final JwtTokenProvider tokens;

    @Before("@within(com.mungdori.fallserver.adapter.security.AdminOnly) || @annotation(com.mungdori.fallserver.adapter.security.AdminOnly)")
    public void checkAdmin() {
        if (!(RequestContextHolder.getRequestAttributes() instanceof ServletRequestAttributes attributes)) {
            throw AuthException.unauthorized();
        }
        String header = attributes.getRequest().getHeader("Authorization");
        if (header == null || !header.regionMatches(true, 0, "Bearer ", 0, 7)) {
            throw AuthException.unauthorized();
        }
        if (tokens.getRole(header.substring(7)) != Role.ADMIN) {
            throw new AuthException(HttpStatus.FORBIDDEN, "관리자만 사용할 수 있습니다.");
        }
    }
}

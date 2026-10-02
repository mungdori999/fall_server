package com.mungdori.fallserver.adapter.filter;

import com.mungdori.fallserver.adapter.exception.AuthException;
import com.mungdori.fallserver.adapter.security.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class AccessTokenAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider tokens;
    private final String frontendUrl;

    public AccessTokenAuthenticationFilter(JwtTokenProvider tokens,
                                           @Value("${app.frontend-url}") String frontendUrl) {
        this.tokens = tokens;
        this.frontendUrl = frontendUrl;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !request.getRequestURI().startsWith("/api/")
                || request.getMethod().equalsIgnoreCase("OPTIONS")
                || request.getRequestURI().startsWith("/api/login/")
                || (request.getMethod().equalsIgnoreCase("POST")
                    && request.getRequestURI().equals("/api/admin"));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorization == null || !authorization.regionMatches(true, 0, "Bearer ", 0, 7)) {
            writeUnauthorized(request, response);
            return;
        }

        try {
            tokens.getAuthMember(authorization.substring(7));
            filterChain.doFilter(request, response);
        } catch (AuthException exception) {
            writeUnauthorized(request, response);
        }
    }

    private void writeUnauthorized(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (frontendUrl.equals(request.getHeader(HttpHeaders.ORIGIN))) {
            response.setHeader("Access-Control-Allow-Origin", frontendUrl);
        }
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/problem+json;charset=UTF-8");
        response.getWriter().write("{\"status\":401,\"detail\":\"로그인 정보 또는 토큰이 올바르지 않습니다.\"}");
    }
}

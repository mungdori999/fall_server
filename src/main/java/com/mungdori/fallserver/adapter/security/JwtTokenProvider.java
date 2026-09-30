package com.mungdori.fallserver.adapter.security;

import com.mungdori.fallserver.adapter.exception.AuthException;
import com.mungdori.fallserver.adapter.webapi.dto.TokenResponse;
import com.mungdori.fallserver.domain.admin.Admin;
import com.mungdori.fallserver.domain.auth.Role;
import com.mungdori.fallserver.domain.member.Member;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;

@Component
public class JwtTokenProvider {
    private final SecretKey key;
    private final long expiresIn;

    public JwtTokenProvider(@Value("${jwt.secret:}") String secret,
                            @Value("${jwt.expires-in:2592000}") long expiresIn) {
        if (expiresIn <= 0) throw new IllegalArgumentException("jwt.expires-in must be positive");
        this.expiresIn = expiresIn;
        if (secret.isBlank()) {
            key = Jwts.SIG.HS256.key().build();
            LoggerFactory.getLogger(getClass()).warn("JWT_SECRET 미설정: 임시 서명 키를 사용합니다. 재시작하면 기존 토큰이 만료됩니다.");
        } else {
            key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
        }
    }

    public TokenResponse issueMember(Member member) {
        Instant now = Instant.now();
        String token = Jwts.builder().claim("role", Role.MEMBER)
                .claim("id", member.getId())
                .claim("name", member.getName())
                .issuedAt(Date.from(now)).expiration(Date.from(now.plusSeconds(expiresIn)))
                .signWith(key, Jwts.SIG.HS256).compact();
        return new TokenResponse(token, "Bearer", expiresIn);
    }

    public TokenResponse issueAdmin(Admin admin) {
        Instant now = Instant.now();
        String token = Jwts.builder().claim("role", Role.ADMIN)
                .claim("id", admin.getId())
                .claim("name", admin.getName())
                .issuedAt(Date.from(now)).expiration(Date.from(now.plusSeconds(expiresIn)))
                .signWith(key, Jwts.SIG.HS256).compact();
        return new TokenResponse(token, "Bearer", expiresIn);
    }

    public Role getRole(String token) {
        try {
            return getAuthMember(token).role();
        } catch (JwtException | IllegalArgumentException e) {
            throw AuthException.unauthorized();
        }
    }

    public AuthMember getAuthMember(String token) {
        try {
            var claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
            if (claims.getExpiration() == null) throw AuthException.unauthorized();

            Long id = claims.get("id", Long.class);
            String name = claims.get("name", String.class);
            String role = claims.get("role", String.class);
            if (id == null || name == null || role == null) throw AuthException.unauthorized();

            return new AuthMember(id, name, Role.valueOf(role));
        } catch (JwtException | IllegalArgumentException e) {
            throw AuthException.unauthorized();
        }
    }


}

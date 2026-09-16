package com.mungdori.fallserver.adapter.webapi;

import com.mungdori.fallserver.adapter.exception.AuthException;
import com.mungdori.fallserver.adapter.security.JwtTokenProvider;
import com.mungdori.fallserver.adapter.webapi.dto.TokenResponse;
import com.mungdori.fallserver.application.admin.required.AdminRepository;
import com.mungdori.fallserver.application.member.required.MemberRepository;
import com.mungdori.fallserver.domain.admin.PasswordEncoder;
import com.mungdori.fallserver.domain.auth.AdminLogin;
import com.mungdori.fallserver.domain.auth.MemberLogin;
import com.mungdori.fallserver.domain.auth.Role;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LoginApi {
    private final MemberRepository members;
    private final AdminRepository admins;
    private final PasswordEncoder passwords;
    private final JwtTokenProvider tokens;

    @PostMapping({"/api/member/login", "/api/members/login"})
    public ResponseEntity<TokenResponse> member(@Valid @RequestBody MemberLogin request) {
        if (!members.existsByCode(request.code())) throw AuthException.unauthorized();
        return response(Role.MEMBER);
    }

    @PostMapping("/api/admin/login")
    public ResponseEntity<TokenResponse> admin(@Valid @RequestBody AdminLogin request) {
        var admin = admins.findByLoginId(request.loginId()).orElseThrow(AuthException::unauthorized);
        if (!admin.verifyPassword(request.password(), passwords)) throw AuthException.unauthorized();
        return response(Role.ADMIN);
    }

    private ResponseEntity<TokenResponse> response(Role role) {
        return ResponseEntity.ok().cacheControl(CacheControl.noStore()).body(tokens.issue(role));
    }


}

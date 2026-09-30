package com.mungdori.fallserver.adapter.webapi;

import com.mungdori.fallserver.adapter.exception.AuthException;
import com.mungdori.fallserver.adapter.security.JwtTokenProvider;
import com.mungdori.fallserver.adapter.webapi.dto.TokenResponse;
import com.mungdori.fallserver.application.admin.required.AdminRepository;
import com.mungdori.fallserver.application.member.required.MemberRepository;
import com.mungdori.fallserver.domain.admin.Admin;
import com.mungdori.fallserver.domain.admin.PasswordEncoder;
import com.mungdori.fallserver.domain.auth.AdminLogin;
import com.mungdori.fallserver.domain.auth.MemberLogin;
import com.mungdori.fallserver.domain.auth.Role;
import com.mungdori.fallserver.domain.member.Member;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/login")
@RequiredArgsConstructor
public class LoginApi {
    private final MemberRepository members;
    private final AdminRepository admins;
    private final PasswordEncoder passwords;
    private final JwtTokenProvider tokens;

    @PostMapping("/member")
    public ResponseEntity<TokenResponse> member(@Valid @RequestBody MemberLogin request) {
        Member member = members.findByName(request.name()).orElseThrow(AuthException::unauthorized);
        if (!member.verifyCode(request.code())) throw AuthException.unauthorized();
        return ResponseEntity.ok().cacheControl(CacheControl.noStore()).body(tokens.issueMember(member));
    }

    @PostMapping("/admin")
    public ResponseEntity<TokenResponse> admin(@Valid @RequestBody AdminLogin request) {
        Admin admin = admins.findByLoginId(request.loginId()).orElseThrow(AuthException::unauthorized);
        if (!admin.verifyPassword(request.password(), passwords)) throw AuthException.unauthorized();
        return ResponseEntity.ok().cacheControl(CacheControl.noStore()).body(tokens.issueAdmin(admin));
    }

}

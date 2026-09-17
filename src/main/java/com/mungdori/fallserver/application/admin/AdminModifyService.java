package com.mungdori.fallserver.application.admin;

import com.mungdori.fallserver.adapter.exception.AuthException;
import com.mungdori.fallserver.application.admin.provided.AdminCommand;
import com.mungdori.fallserver.application.admin.required.AdminCodeRepository;
import com.mungdori.fallserver.application.admin.required.AdminRepository;
import com.mungdori.fallserver.domain.admin.Admin;
import com.mungdori.fallserver.domain.admin.AdminRegisterRequest;
import com.mungdori.fallserver.domain.admin.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@Transactional
@Validated
@RequiredArgsConstructor
public class AdminModifyService implements AdminCommand {

    private final AdminRepository adminRepository;
    private final AdminCodeRepository adminCodeRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Admin register(AdminRegisterRequest registerRequest) {

        boolean validCode = adminCodeRepository.existsByCode(registerRequest.code());

        if (!validCode) {
            throw new AuthException(HttpStatus.FORBIDDEN, "관리자만 사용할 수 있습니다.");
        }

        Admin admin = Admin.register(registerRequest, passwordEncoder);

        adminRepository.save(admin);

        return admin;
    }
}

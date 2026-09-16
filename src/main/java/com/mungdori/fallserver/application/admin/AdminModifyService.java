package com.mungdori.fallserver.application.admin;

import com.mungdori.fallserver.application.admin.provided.AdminCommand;
import com.mungdori.fallserver.application.admin.required.AdminRepository;
import com.mungdori.fallserver.domain.admin.Admin;
import com.mungdori.fallserver.domain.admin.AdminRegisterRequest;
import com.mungdori.fallserver.domain.admin.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@Transactional
@Validated
@RequiredArgsConstructor
public class AdminModifyService implements AdminCommand {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Admin register(AdminRegisterRequest registerRequest) {
        Admin admin = Admin.register(registerRequest, passwordEncoder);

        adminRepository.save(admin);

        return admin;
    }
}

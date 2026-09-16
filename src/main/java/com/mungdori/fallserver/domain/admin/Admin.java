package com.mungdori.fallserver.domain.admin;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

import static java.util.Objects.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String loginId;
    private String passwordHash;


    public static Admin register(AdminRegisterRequest request, PasswordEncoder passwordEncoder) {
        Admin admin = new Admin();

        admin.name = requireNonNull(request.name());
        admin.loginId = requireNonNull(request.loginId());
        admin.passwordHash = passwordEncoder.encode(requireNonNull(request.password()));

        return admin;
    }

    public boolean verifyPassword(String password, PasswordEncoder passwordEncoder) {
        return passwordEncoder.matches(password, this.passwordHash);
    }


}

package com.mungdori.fallserver.domain.admin;

public interface PasswordEncoder {
    String encode (String password);
    boolean matches (String password, String passwordHash);
}

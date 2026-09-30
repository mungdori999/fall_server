package com.mungdori.fallserver.adapter.security;

import com.mungdori.fallserver.domain.auth.Role;

public record AuthMember(Long id, String name, Role role) {
}

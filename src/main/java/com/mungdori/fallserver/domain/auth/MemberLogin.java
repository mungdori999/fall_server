package com.mungdori.fallserver.domain.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record MemberLogin(@NotBlank @Size(max = 255) String code) {
}

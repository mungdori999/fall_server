package com.mungdori.fallserver.domain.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AdminLogin(@NotNull String loginId, @NotBlank @Size(max = 72) String password) {
}

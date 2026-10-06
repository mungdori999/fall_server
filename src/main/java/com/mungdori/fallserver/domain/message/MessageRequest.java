package com.mungdori.fallserver.domain.message;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record MessageRequest(
        @NotNull Long receiverMemberId,
        @NotBlank @Size(max = 30) String senderName,
        @NotBlank @Size(max = 250) String content) {
}

package com.mungdori.fallserver.adapter.webapi.dto;

import com.mungdori.fallserver.domain.message.Message;

public record MessageResponse(
        Long id,
        String senderName,
        String receiverName,
        String content
) {
    public static MessageResponse of(Message message, String receiverName) {
        return new MessageResponse(
                message.getId(),
                message.getSenderName(),
                receiverName,
                message.getContent()
        );
    }
}

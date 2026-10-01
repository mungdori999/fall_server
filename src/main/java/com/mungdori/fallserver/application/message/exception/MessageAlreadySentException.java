package com.mungdori.fallserver.application.message.exception;

public class MessageAlreadySentException extends RuntimeException {
    public MessageAlreadySentException() {
        super("이미 이 참가자에게 쪽지를 보냈습니다.");
    }
}

package com.mungdori.fallserver.adapter.webapi.dto;

public record TokenResponse(String accessToken, String tokenType, long expiresIn) {

}

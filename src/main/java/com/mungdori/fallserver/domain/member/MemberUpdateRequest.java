package com.mungdori.fallserver.domain.member;

public record MemberUpdateRequest(

        String name,
        Gender gender,
        String code) {

}


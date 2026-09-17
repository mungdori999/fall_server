package com.mungdori.fallserver.domain.member;


public record MemberRegisterRequest(

        String name,
        Gender gender,
        String code) {

}

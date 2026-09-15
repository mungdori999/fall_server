package com.mungdori.fallserver.adapter.webapi.dto;


import com.mungdori.fallserver.domain.member.Member;

public record MemberRegisterResponse(Long memberId, String code) {
    public static MemberRegisterResponse of(Member member) {
        return new MemberRegisterResponse(member.getId(), member.getCode());
    }
}

package com.mungdori.fallserver.adapter.webapi.dto;

import com.mungdori.fallserver.domain.member.Gender;
import com.mungdori.fallserver.domain.member.Member;

public record MemberResponse(
        Long id,
        String name,
        Gender gender,
        String code

) {

    public static MemberResponse of(Member member) {
        return new MemberResponse(member.getId(), member.getName(), member.getGender(), member.getCode());
    }
}

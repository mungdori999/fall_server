package com.mungdori.fallserver.adapter.webapi.dto;

import com.mungdori.fallserver.domain.member.Gender;
import com.mungdori.fallserver.domain.member.Member;

/** 쪽지를 보낼 수 있는 상대 목록에만 사용하는 안전한 회원 응답이다. */
public record MemberCandidateResponse(Long id, String name, Gender gender) {
    public static MemberCandidateResponse of(Member member) {
        return new MemberCandidateResponse(member.getId(), member.getName(), member.getGender());
    }
}

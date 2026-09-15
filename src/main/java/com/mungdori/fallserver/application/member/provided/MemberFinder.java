package com.mungdori.fallserver.application.member.provided;


import com.mungdori.fallserver.domain.member.Member;

/**
 * 회원을 조회한다.
 */
public interface MemberFinder {

    Member find(Long memberId);

}

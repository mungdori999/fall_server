package com.mungdori.fallserver.application.member.provided;


import com.mungdori.fallserver.domain.member.Member;

import java.util.List;

/**
 * 회원을 조회한다.
 */
public interface MemberFinder {

    List<Member> findAll();
    Member find(Long memberId);

}

package com.mungdori.fallserver.application.member.provided;


import com.mungdori.fallserver.domain.member.Member;
import com.mungdori.fallserver.domain.member.MemberRegisterRequest;
import com.mungdori.fallserver.domain.member.MemberUpdateRequest;

import java.util.List;

/**
 * 회원의 등록과 관련된 기능을 제공한다
 */
public interface MemberCommand {

    List<Member> getMemberList();
    Member getMember(Long id);

    Member register(MemberRegisterRequest registerRequest);


    void updateCode(Long id, MemberUpdateRequest request);

    void delete(Long id);
}

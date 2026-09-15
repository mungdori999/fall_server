package com.mungdori.fallserver.application.member;

import com.mungdori.fallserver.application.member.provided.MemberFinder;
import com.mungdori.fallserver.application.member.provided.MemberCommand;
import com.mungdori.fallserver.application.member.required.MemberRepository;
import com.mungdori.fallserver.domain.member.Member;
import com.mungdori.fallserver.domain.member.MemberRegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@Transactional
@Validated
@RequiredArgsConstructor
public class MemberModifyService implements MemberCommand {

    private final MemberFinder memberFinder;
    private final MemberRepository memberRepository;

    @Override
    public Member register(MemberRegisterRequest registerRequest) {

        Member member = Member.register(registerRequest);

        memberRepository.save(member);

        return member;
    }

    @Override
    public void updateCode(Long id, String code) {
        Member member = memberFinder.find(id);

        member = member.updateCode(code);

        memberRepository.save(member);
    }

}

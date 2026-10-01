package com.mungdori.fallserver.application.member;

import com.mungdori.fallserver.application.member.provided.MemberFinder;
import com.mungdori.fallserver.application.member.provided.MemberCommand;
import com.mungdori.fallserver.application.member.required.MemberRepository;
import com.mungdori.fallserver.domain.member.Gender;
import com.mungdori.fallserver.domain.member.Member;
import com.mungdori.fallserver.domain.member.MemberRegisterRequest;
import com.mungdori.fallserver.domain.member.MemberUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@Transactional
@Validated
@RequiredArgsConstructor
public class MemberModifyService implements MemberCommand {

    private final MemberFinder memberFinder;
    private final MemberRepository memberRepository;


    @Override
    public List<Member> getMemberList() {
        return memberFinder.findAll();
    }

    @Override
    public Member getMember(Long id) {
        return memberFinder.find(id);

    }

    @Override
    public List<Member> getMemberListByGender(Long id) {
        Member member = memberFinder.find(id);

        Gender serachGender;
        // 내가 남자면 여자를 조회 여자면 남자를 조회
        if (member.getGender().equals(Gender.MALE)) {
            serachGender = Gender.FEMALE;
        } else {
            serachGender = Gender.MALE;
        }

        return memberFinder.findByGender(serachGender);
    }

    @Override
    public Member register(MemberRegisterRequest registerRequest) {

        Member member = Member.register(registerRequest);

        memberRepository.save(member);

        return member;
    }

    @Override
    public int registerAll(List<MemberRegisterRequest> registerRequests) {
        for (MemberRegisterRequest request : registerRequests) {
            if (memberRepository.existsByCode(request.code())) {
                throw new IllegalArgumentException("이미 등록된 참가 코드가 있습니다: " + request.code());
            }
        }

        for (MemberRegisterRequest request : registerRequests) {
            memberRepository.save(Member.register(request));
        }

        return registerRequests.size();
    }

    @Override
    public void updateCode(Long id, MemberUpdateRequest request) {
        Member member = memberFinder.find(id);

        member = member.updateCode(request);

        memberRepository.save(member);
    }

    @Override
    public void delete(Long id) {
        Member member = memberFinder.find(id);

        memberRepository.delete(member);
    }

}

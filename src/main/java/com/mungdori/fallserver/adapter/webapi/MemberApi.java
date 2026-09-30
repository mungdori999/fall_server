package com.mungdori.fallserver.adapter.webapi;


import com.mungdori.fallserver.adapter.security.AdminOnly;
import com.mungdori.fallserver.adapter.security.AuthMember;
import com.mungdori.fallserver.adapter.security.CurrentMember;
import com.mungdori.fallserver.adapter.webapi.dto.MemberCandidateResponse;
import com.mungdori.fallserver.adapter.webapi.dto.MessageCountResponse;
import com.mungdori.fallserver.adapter.webapi.dto.MemberRegisterResponse;
import com.mungdori.fallserver.adapter.webapi.dto.MemberResponse;
import com.mungdori.fallserver.application.member.provided.MemberCommand;
import com.mungdori.fallserver.domain.member.Gender;
import com.mungdori.fallserver.domain.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberApi {

    private final MemberCommand memberCommand;

    @GetMapping("/me")
    public ResponseEntity<MemberResponse> getCurrentMember(@CurrentMember AuthMember currentMember) {
        Member member = memberCommand.getMember(currentMember.id());

        return new ResponseEntity<>(MemberResponse.of(member), HttpStatus.OK);
    }

    @GetMapping("/me/message-count")
    public ResponseEntity<MessageCountResponse> getRemainingMessageCount(
            @CurrentMember AuthMember currentMember
    ) {
        Member member = memberCommand.getMember(currentMember.id());

        return ResponseEntity.ok(new MessageCountResponse(member.getMessageCnt()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MemberResponse> getMember(@PathVariable Long id) {
        Member member = memberCommand.getMember(id);

        return new ResponseEntity<>(MemberResponse.of(member), HttpStatus.OK);
    }

    @GetMapping("/list/gender")
    public ResponseEntity<List<MemberCandidateResponse>> getMemberListByGender(@CurrentMember AuthMember currentMember) {
        List<Member> memberList = memberCommand.getMemberListByGender(currentMember.id());

        return ResponseEntity.ok(memberList.stream().map(MemberCandidateResponse::of).toList());
    }

}

package com.mungdori.fallserver.adapter.webapi;

import com.mungdori.fallserver.adapter.security.AdminOnly;
import com.mungdori.fallserver.adapter.webapi.dto.AdminRegisterResponse;
import com.mungdori.fallserver.adapter.webapi.dto.MemberRegisterResponse;
import com.mungdori.fallserver.adapter.webapi.dto.MemberResponse;
import com.mungdori.fallserver.application.admin.provided.AdminCommand;
import com.mungdori.fallserver.application.member.provided.MemberCommand;
import com.mungdori.fallserver.domain.admin.Admin;
import com.mungdori.fallserver.domain.admin.AdminRegisterRequest;
import com.mungdori.fallserver.domain.member.Member;
import com.mungdori.fallserver.domain.member.MemberRegisterRequest;
import com.mungdori.fallserver.domain.member.MemberUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminApi {

    private final MemberCommand memberCommand;
    private final AdminCommand adminCommand;


    /**
     * 관리자 회원가입
     *
     * @param request
     * @return
     */
    @PostMapping()
    public AdminRegisterResponse registerAdmin(@RequestBody AdminRegisterRequest request) {

        Admin admin = adminCommand.register(request);

        return AdminRegisterResponse.of(admin);
    }

    /**
     * 등록된 회원 전체조회
     *
     * @return
     */
    @AdminOnly
    @GetMapping("/member/list")
    public ResponseEntity<List<MemberResponse>> getMemberList() {
        List<Member> memberList = memberCommand.getMemberList();
        return new ResponseEntity<>(memberList.stream().map(MemberResponse::of).toList(), HttpStatus.OK);
    }

    /**
     * Code랑 회원 등록 동시에
     *
     * @param request
     * @return
     */
    @AdminOnly
    @PostMapping("/member")
    public MemberRegisterResponse registerMember(@RequestBody MemberRegisterRequest request) {
        Member member = memberCommand.register(request);

        return MemberRegisterResponse.of(member);
    }

    /**
     * 멤버 정보 수정
     *
     * @param request
     * @param id
     */
    @AdminOnly
    @PutMapping("/member/{id}")
    public void changeCode(@RequestBody MemberUpdateRequest request,
                           @PathVariable Long id) {
        memberCommand.updateCode(id, request);
    }

    /**
     * 멤버 삭제
     * @param id
     */
    @AdminOnly
    @DeleteMapping("/member/{id}")
    public void deleteMember(@PathVariable Long id) {
        memberCommand.delete(id);
    }



}

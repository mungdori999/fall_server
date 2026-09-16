package com.mungdori.fallserver.adapter.webapi;

import com.mungdori.fallserver.adapter.security.AdminOnly;
import com.mungdori.fallserver.adapter.webapi.dto.AdminRegisterResponse;
import com.mungdori.fallserver.adapter.webapi.dto.MemberRegisterResponse;
import com.mungdori.fallserver.application.admin.provided.AdminCommand;
import com.mungdori.fallserver.application.member.provided.MemberCommand;
import com.mungdori.fallserver.domain.admin.Admin;
import com.mungdori.fallserver.domain.admin.AdminRegisterRequest;
import com.mungdori.fallserver.domain.member.Member;
import com.mungdori.fallserver.domain.member.MemberRegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@com.mungdori.fallserver.adapter.security.AdminOnly
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminApi {

    private final MemberCommand memberCommand;
    private final AdminCommand adminCommand;


    /**
     * 관리자 회원가입
     * @param request
     * @return
     */
    @PostMapping()
    public AdminRegisterResponse registerAdmin(@RequestBody AdminRegisterRequest request) {

        Admin admin = adminCommand.register(request);

        return AdminRegisterResponse.of(admin);
    }

    /**
     * Code랑 회원 등록 동시에
     * @param request
     * @return
     */
    @AdminOnly
    @PostMapping("/members")
    public MemberRegisterResponse registerMember(@RequestBody MemberRegisterRequest request) {
        Member member = memberCommand.register(request);

        return MemberRegisterResponse.of(member);
    }

    /**
     * Code 수정
     * @param code
     * @param id
     */
    @AdminOnly
    @PutMapping("/code/{id}")
    public void changeCode(@RequestParam("code") String code,
                           @PathVariable Long id) {
        memberCommand.updateCode(id, code);
    }

}

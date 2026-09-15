package com.mungdori.fallserver.adapter.webapi;


import com.mungdori.fallserver.adapter.webapi.dto.MemberRegisterResponse;
import com.mungdori.fallserver.application.member.provided.MemberCommand;
import com.mungdori.fallserver.domain.member.Member;
import com.mungdori.fallserver.domain.member.MemberRegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MemberApi {

    private final MemberCommand memberCommand;


    /**
     * Code랑 회원 등록 동시에
     * @param request
     * @return
     */
    @PostMapping("/members")
    public MemberRegisterResponse register(@RequestBody MemberRegisterRequest request) {
        Member member = memberCommand.register(request);

        return MemberRegisterResponse.of(member);
    }

    /**
     * Code 수정
     * @param code
     * @param id
     */
    @PutMapping("/members/code/{id}")
    public void changeCode(@RequestParam("code") String code,
                           @PathVariable Long id) {
            memberCommand.updateCode(id, code);
    }


}

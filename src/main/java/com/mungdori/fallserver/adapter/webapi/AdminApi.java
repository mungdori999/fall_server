package com.mungdori.fallserver.adapter.webapi;

import com.mungdori.fallserver.adapter.security.AdminOnly;
import com.mungdori.fallserver.adapter.webapi.dto.AdminRegisterResponse;
import com.mungdori.fallserver.adapter.webapi.dto.MemberRegisterResponse;
import com.mungdori.fallserver.adapter.webapi.dto.MemberBulkRegisterResponse;
import com.mungdori.fallserver.adapter.webapi.dto.MemberResponse;
import com.mungdori.fallserver.application.admin.provided.AdminCommand;
import com.mungdori.fallserver.application.admin.provided.MemberExcelParser;
import com.mungdori.fallserver.application.admin.provided.MemberExcelTemplateGenerator;
import com.mungdori.fallserver.application.member.provided.MemberCommand;
import com.mungdori.fallserver.domain.admin.Admin;
import com.mungdori.fallserver.domain.admin.AdminRegisterRequest;
import com.mungdori.fallserver.domain.member.Member;
import com.mungdori.fallserver.domain.member.MemberRegisterRequest;
import com.mungdori.fallserver.domain.member.MemberUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.io.IOException;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminApi {

    private final MemberCommand memberCommand;
    private final AdminCommand adminCommand;
    private final MemberExcelParser memberExcelParser;
    private final MemberExcelTemplateGenerator memberExcelTemplateGenerator;


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

    @AdminOnly
    @PostMapping(value = "/member/bulk", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MemberBulkRegisterResponse> registerMembersFromExcel(
            @RequestPart("file") MultipartFile file
    ) {
        String filename = file.getOriginalFilename();
        if (file.isEmpty() || filename == null || !filename.toLowerCase().endsWith(".xlsx")) {
            throw new IllegalArgumentException(".xlsx 파일만 업로드할 수 있습니다.");
        }

        try {
            int registeredCount = memberCommand.registerAll(memberExcelParser.parse(file.getInputStream()));
            return ResponseEntity.status(HttpStatus.CREATED).body(new MemberBulkRegisterResponse(registeredCount));
        } catch (IOException exception) {
            throw new IllegalArgumentException("업로드 파일을 읽을 수 없습니다.", exception);
        }
    }

    @AdminOnly
    @GetMapping("/member/template")
    public ResponseEntity<byte[]> downloadMemberExcelTemplate() {
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=member-import-template.xlsx")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(memberExcelTemplateGenerator.generate());
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

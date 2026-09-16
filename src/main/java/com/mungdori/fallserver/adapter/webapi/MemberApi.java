package com.mungdori.fallserver.adapter.webapi;


import com.mungdori.fallserver.application.member.provided.MemberCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ap/members")
public class MemberApi {

    private final MemberCommand memberCommand;



}

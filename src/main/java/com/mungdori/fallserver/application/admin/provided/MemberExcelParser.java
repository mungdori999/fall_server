package com.mungdori.fallserver.application.admin.provided;

import com.mungdori.fallserver.domain.member.MemberRegisterRequest;

import java.io.InputStream;
import java.util.List;

/** 관리자용 회원 일괄 등록 파일을 참가자 등록 요청 목록으로 변환한다. */
public interface MemberExcelParser {
    List<MemberRegisterRequest> parse(InputStream inputStream);
}

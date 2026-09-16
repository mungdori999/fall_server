package com.mungdori.fallserver.application.admin.provided;

import com.mungdori.fallserver.domain.admin.Admin;
import com.mungdori.fallserver.domain.admin.AdminRegisterRequest;
import com.mungdori.fallserver.domain.member.Member;
import com.mungdori.fallserver.domain.member.MemberRegisterRequest;

public interface AdminCommand {

    Admin register(AdminRegisterRequest registerRequest);
}

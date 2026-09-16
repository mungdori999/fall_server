package com.mungdori.fallserver.application.admin.required;


import com.mungdori.fallserver.domain.admin.Admin;
import com.mungdori.fallserver.domain.member.Member;
import org.springframework.data.repository.Repository;

import java.util.Optional;

/**
 * 회원정보를 저장하거나 조회한다
 */
public interface AdminRepository extends Repository<Admin, Long> {
    Admin save(Admin admin);

    Optional<Admin> findByLoginId(String adminId);


}

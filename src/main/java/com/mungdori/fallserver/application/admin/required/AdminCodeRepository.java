package com.mungdori.fallserver.application.admin.required;

import com.mungdori.fallserver.domain.admin.AdminCode;
import org.springframework.data.repository.Repository;

public interface AdminCodeRepository extends Repository<AdminCode, Long> {

    void save(AdminCode adminCode);

    boolean existsByCode(String code);
}

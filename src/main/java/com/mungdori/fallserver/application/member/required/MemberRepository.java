package com.mungdori.fallserver.application.member.required;


import com.mungdori.fallserver.domain.member.Member;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 회원정보를 저장하거나 조회한다
 */
public interface MemberRepository extends Repository<Member, Long> {
    Member save(Member member);

    boolean existsByCode(String code);

    Optional<Member> findById(Long memberId);

    Optional<Member> findByName(String name);

    List<Member> findAll();


    void delete(Member member);
}

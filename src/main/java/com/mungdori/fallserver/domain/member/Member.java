package com.mungdori.fallserver.domain.member;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static java.util.Objects.requireNonNull;


@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String code;

    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private MemberDetail detail;

    public static Member register(MemberRegisterRequest createRequest) {
        Member member = new Member();

        member.name = requireNonNull(createRequest.name());
        member.code = requireNonNull(createRequest.code());

        member.detail = MemberDetail.create();

        return member;
    }

    public Member updateCode(String code) {
        this.code = requireNonNull(code);
        return this;
    }
}

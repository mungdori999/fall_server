package com.mungdori.fallserver.domain.member;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

import static java.util.Objects.requireNonNull;


@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String code;


    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private MemberDetail detail;

    public static Member register(MemberRegisterRequest createRequest) {
        Member member = new Member();

        member.name = requireNonNull(createRequest.name());
        member.gender = requireNonNull(createRequest.gender());
        member.code = requireNonNull(createRequest.code());

        member.detail = MemberDetail.create(member);

        return member;
    }

    public Member updateCode(MemberUpdateRequest updateRequest) {
        this.code = requireNonNull(updateRequest.code());
        this.name = requireNonNull(updateRequest.name());
        this.gender = requireNonNull(updateRequest.gender());

        return this;
    }

    public boolean verifyCode(String code) {
        return Objects.equals(this.code, requireNonNull(code));
    }

}

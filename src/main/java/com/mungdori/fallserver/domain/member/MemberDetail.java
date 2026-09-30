package com.mungdori.fallserver.domain.member;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static java.util.Objects.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime registeredAt;

    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;


    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", nullable = false, unique = true)
    private Member member;


    protected static MemberDetail create(Member member) {
        MemberDetail detail = new MemberDetail();
        detail.member = requireNonNull(member);
        detail.registeredAt = LocalDateTime.now();
        return detail;
    }

}

package com.mungdori.fallserver.domain.message;

import com.mungdori.fallserver.domain.member.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;

import static java.util.Objects.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MessageDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;


    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "message_id", nullable = false, unique = true)
    private Message message;


    protected static MessageDetail create(Message message) {
        MessageDetail messageDetail = new MessageDetail();
        messageDetail.message = requireNonNull(message);
        messageDetail.createdAt = LocalDateTime.now();
        return messageDetail;
    }

}

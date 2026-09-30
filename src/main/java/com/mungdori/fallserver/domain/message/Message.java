package com.mungdori.fallserver.domain.message;

import com.mungdori.fallserver.domain.member.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

import static java.util.Objects.*;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long senderMemberId;
    private Long receiverMemberId;
    private String senderName;
    private String content;

    @OneToOne(mappedBy = "message", cascade = CascadeType.ALL, orphanRemoval = true)
    private MessageDetail detail;

    public static Message create(Member sender, Member receiver, MessageRequest createRequest) {
        Message message = new Message();

        message.senderMemberId = requireNonNull(sender.getId());
        message.receiverMemberId = requireNonNull(receiver.getId());
        message.senderName = requireNonNull(createRequest.senderName()).trim();
        message.content = requireNonNull(createRequest.content());

        message.detail = MessageDetail.create(message);

        return message;
    }
}

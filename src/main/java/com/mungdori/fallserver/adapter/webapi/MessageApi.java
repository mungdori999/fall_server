package com.mungdori.fallserver.adapter.webapi;

import com.mungdori.fallserver.adapter.security.AuthMember;
import com.mungdori.fallserver.adapter.security.CurrentMember;
import com.mungdori.fallserver.adapter.webapi.dto.MessageResponse;
import com.mungdori.fallserver.application.message.provided.MessageCommand;
import com.mungdori.fallserver.application.member.provided.MemberCommand;
import com.mungdori.fallserver.domain.message.Message;
import com.mungdori.fallserver.domain.message.MessageRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/message")
public class MessageApi {

    private final MessageCommand messageCommand;
    private final MemberCommand memberCommand;

    @GetMapping("/sent")
    public ResponseEntity<List<MessageResponse>> getSentMessages(
            @CurrentMember AuthMember currentMember
    ) {
        List<MessageResponse> messages = messageCommand.getMessageList(currentMember.id())
                .stream()
                .map(message -> MessageResponse.of(
                        message,
                        memberCommand.getMember(message.getReceiverMemberId()).getName()
                ))
                .toList();

        return ResponseEntity.ok(messages);
    }

    @GetMapping("/received")
    public ResponseEntity<List<MessageResponse>> getReceivedMessages(
            @CurrentMember AuthMember currentMember
    ) {
        List<MessageResponse> messages = messageCommand.getReceivedMessageList(currentMember.id())
                .stream()
                .map(message -> MessageResponse.of(
                        message,
                        memberCommand.getMember(message.getReceiverMemberId()).getName()
                ))
                .toList();

        return ResponseEntity.ok(messages);
    }

    @PostMapping("")
    public ResponseEntity<Void> sendMessage(@Valid @RequestBody MessageRequest request, @CurrentMember AuthMember currentMember) {
        messageCommand.saveMessage(request, currentMember.id());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}

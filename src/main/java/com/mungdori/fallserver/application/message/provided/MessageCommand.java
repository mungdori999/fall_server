package com.mungdori.fallserver.application.message.provided;

import com.mungdori.fallserver.domain.message.Message;
import com.mungdori.fallserver.domain.message.MessageRequest;

import java.util.List;

public interface MessageCommand {

    List<Message> getMessageList(Long senderMemberId);

    List<Message> getReceivedMessageList(Long receiverMemberId);

    void saveMessage(MessageRequest request, Long memberId);
}

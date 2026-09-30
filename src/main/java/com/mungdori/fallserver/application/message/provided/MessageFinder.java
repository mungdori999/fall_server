package com.mungdori.fallserver.application.message.provided;

import com.mungdori.fallserver.domain.message.Message;

import java.util.List;

public interface MessageFinder {

    Message findById(Long messageId);

    List<Message> findAllBySenderMemberId(Long senderMemberId);

    List<Message> findAllByReceiverMemberId(Long receiverMemberId);
}

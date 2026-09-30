package com.mungdori.fallserver.application.message.required;

import com.mungdori.fallserver.domain.message.Message;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface MessageRepository extends Repository<Message, Long> {
    void save(Message message);

    Optional<Message> findById(Long messageId);

    List<Message> findAllBySenderMemberId(Long senderMemberId);

    List<Message> findAllByReceiverMemberId(Long receiverMemberId);
}

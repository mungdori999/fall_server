package com.mungdori.fallserver.application.message;

import com.mungdori.fallserver.application.message.provided.MessageFinder;
import com.mungdori.fallserver.application.message.required.MessageRepository;
import com.mungdori.fallserver.domain.message.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MessageQueryService implements MessageFinder {

    private final MessageRepository messageRepository;


    @Override
    public Message findById(Long messageId) {
        return messageRepository.findById(messageId)
                .orElseThrow(() -> new IllegalArgumentException("메세지를 찾을 수 없습니다." + messageId));
    }

    @Override
    public List<Message> findAllBySenderMemberId(Long senderMemberId) {
        return messageRepository.findAllBySenderMemberId(senderMemberId);
    }

    @Override
    public List<Message> findAllByReceiverMemberId(Long receiverMemberId) {
        return messageRepository.findAllByReceiverMemberId(receiverMemberId);
    }
}

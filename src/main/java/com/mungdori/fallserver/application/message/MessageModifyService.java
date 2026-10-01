package com.mungdori.fallserver.application.message;

import com.mungdori.fallserver.application.member.provided.MemberFinder;
import com.mungdori.fallserver.application.message.provided.MessageCommand;
import com.mungdori.fallserver.application.message.provided.MessageFinder;
import com.mungdori.fallserver.application.message.required.MessageRepository;
import com.mungdori.fallserver.application.message.exception.MessageAlreadySentException;
import com.mungdori.fallserver.domain.member.Member;
import com.mungdori.fallserver.domain.message.Message;
import com.mungdori.fallserver.domain.message.MessageRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MessageModifyService implements MessageCommand {

    private final MessageRepository messageRepository;
    private final MessageFinder messageFinder;
    private final MemberFinder memberFinder;


    @Override
    public List<Message> getMessageList(Long senderMemberId) {
        return messageFinder.findAllBySenderMemberId(senderMemberId);
    }

    @Override
    public List<Message> getReceivedMessageList(Long receiverMemberId) {
        return messageFinder.findAllByReceiverMemberId(receiverMemberId);
    }

    @Override
    public void saveMessage(MessageRequest request, Long memberId) {
        Member sender = memberFinder.find(memberId);
        Member receiver = memberFinder.find(request.receiverMemberId());

        if (sender.getId().equals(receiver.getId()) || sender.getGender() == receiver.getGender()) {
            throw new IllegalArgumentException("반대 성별 참가자에게만 쪽지를 보낼 수 있습니다.");
        }

        if (messageRepository.existsBySenderMemberIdAndReceiverMemberId(sender.getId(), receiver.getId())) {
            throw new MessageAlreadySentException();
        }

        sender.useMessageChance();
        Message message = Message.create(sender, receiver, request);

        messageRepository.save(message);
    }
}

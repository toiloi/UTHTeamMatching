package org.example.uthteammatching.services;

import org.example.uthteammatching.models.ChatMessage;
import org.example.uthteammatching.models.UthUser;
import org.example.uthteammatching.repositories.ChatMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatMessageService {
    @Autowired
    private ChatMessageRepository chatMessageRepository;

    public ChatMessage getLastMessageBetween(Long userId1, Long userId2) {
        List<ChatMessage> messages = chatMessageRepository.findConversation(userId1, userId2);
        if (!messages.isEmpty()) {
            return messages.get(messages.size() - 1);
        }
        return null;
    }
}

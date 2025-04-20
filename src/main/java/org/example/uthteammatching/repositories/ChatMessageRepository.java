package org.example.uthteammatching.repositories;

import org.example.uthteammatching.models.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    @Query("SELECT new org.example.uthteammatching.models.ChatMessage(m.senderId, m.receiverId, m.senderName, m.content) " +
            "FROM ChatMessage m " +
            "WHERE (m.senderId = :senderId AND m.receiverId = :receiverId) " +
            "   OR (m.senderId = :receiverId AND m.receiverId = :senderId) " +
            "ORDER BY m.id ASC")
    List<ChatMessage> findConversation(@Param("senderId") Long senderId, @Param("receiverId") Long receiverId);
    List<ChatMessage> findByGroupId(Long groupId);

}

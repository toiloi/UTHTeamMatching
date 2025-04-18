package org.example.uthteammatching.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long senderId;
    private Long receiverId;

    @Column(columnDefinition = "NVARCHAR(100)")
    private String senderName;

    @Column(columnDefinition = "NVARCHAR(1000)")
    private String content;

    @Column(nullable = false)
    private ZonedDateTime timestamp;

    @Enumerated(EnumType.STRING)
    private MessageStatus status;

    private Long groupId;


    public ChatMessage(Long senderId, Long receiverId, String senderName, String content) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.senderName = senderName;
        this.content = content;
    }

    public enum MessageStatus {
        SENT, DELIVERED, READ
    }
}

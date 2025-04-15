package org.example.uthteammatching.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {
    private Long senderId;
    private Long receiverId;
    private String senderName;
    private String content;
}

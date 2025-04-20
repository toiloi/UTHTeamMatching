package org.example.uthteammatching.dto;


import lombok.Getter;
import lombok.Setter;
import org.example.uthteammatching.models.ChatMessage;

@Getter
@Setter
public class FriendDTO {
    private String ho;
    private String ten;
    private String avatar;
    private ChatMessage lastMessage;
    public FriendDTO(String ho, String ten, String avatar) {
        this.ho = ho;
        this.ten = ten;
        this.avatar = avatar;
    }
    public FriendDTO() {

    }
}

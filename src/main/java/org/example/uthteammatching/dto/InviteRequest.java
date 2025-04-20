package org.example.uthteammatching.dto;

public class InviteRequest {
    private Long userId;
    private Long groupId;

    // Getters và setters
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }
}
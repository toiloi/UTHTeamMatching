package org.example.uthteammatching.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "NVARCHAR(1000)")
    private String message;

    private boolean seen = false;

    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UthUser user;


    @ManyToOne
    @JoinColumn(name = "from_user_id") // Người gửi thông báo
    private UthUser userFrom;

    @Enumerated(EnumType.STRING)
    private NotificationType type;

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    private Long groupId;


    public Notification(String message, UthUser userId, UthUser userFrom, NotificationType type) {
        this.message = message;
        this.user = userId;
        this.userFrom = userFrom;
        this.type = type;
        this.createdAt = LocalDateTime.now();
    }
    public Notification() {}

}

package org.example.uthteammatching.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "chat_groups")
@Getter
@Setter
public class ChatGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long groupId; // Dùng groupId làm khóa chính

    @Column(name = "group_name", columnDefinition = "NVARCHAR(500)")
    private String groupName;

    @ManyToMany
    @JoinTable(
            name = "chat_group_members",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<UthUser> members = new ArrayList<>();

    public void addMember(UthUser user) {
        if (!members.contains(user)) {
            members.add(user);
        }
    }

    public void removeMember(UthUser user) {
        members.remove(user);
    }

    public String getTenProject() {
        return groupName;
    }
}
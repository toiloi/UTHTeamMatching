package org.example.uthteammatching.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class ChatGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "NVARCHAR(500)")
    private String groupName;

    // Mã định danh duy nhất cho nhóm (để gom các thành viên cùng 1 nhóm)
    private Long groupId;

    @ManyToMany
    private List<UthUser> members = new ArrayList<>();

    public void addMember(UthUser user) {
        if (!members.contains(user)) {
            members.add(user);
        }
    }

    // Optional: Phương thức xóa thành viên
    public void removeMember(UthUser user) {
        members.remove(user);
    }

}

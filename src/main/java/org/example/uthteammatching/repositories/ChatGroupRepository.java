package org.example.uthteammatching.repositories;

import org.example.uthteammatching.models.ChatGroup;
import org.example.uthteammatching.models.UthUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatGroupRepository extends JpaRepository<ChatGroup, Long> {
    @Query("SELECT DISTINCT c.groupId, c.groupName FROM ChatGroup c WHERE :user MEMBER OF c.members")
    List<Object[]> findGroupInfoByUser(@Param("user") UthUser user);
    ChatGroup findByGroupId(Long groupId);


}

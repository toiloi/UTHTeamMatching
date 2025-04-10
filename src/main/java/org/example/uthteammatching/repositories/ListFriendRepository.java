package org.example.uthteammatching.repositories;

import org.example.uthteammatching.models.ListFriend;
import org.example.uthteammatching.models.UthUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ListFriendRepository extends JpaRepository<ListFriend, Long> {
    List<ListFriend> findByUserId1OrUserId2(UthUser userId1, UthUser userId2);
}
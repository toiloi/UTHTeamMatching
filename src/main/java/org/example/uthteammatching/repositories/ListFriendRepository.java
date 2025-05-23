package org.example.uthteammatching.repositories;

import org.example.uthteammatching.models.ListFriend;
import org.example.uthteammatching.models.ListFriendId;
import org.example.uthteammatching.models.UthUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ListFriendRepository extends JpaRepository<ListFriend, ListFriendId> {
    @Query("SELECT lf FROM ListFriend lf JOIN FETCH lf.userId1 JOIN FETCH lf.userId2 WHERE lf.userId1 = :user OR lf.userId2 = :user")
    List<ListFriend> findByUserId1OrUserId2WithFetch(@Param("user") UthUser user);
    boolean existsByUserId1AndUserId2(UthUser currentUser, UthUser foundUser);
}
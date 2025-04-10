package org.example.uthteammatching.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "listFriend")
public class ListFriend {
    @EmbeddedId
    private ListFriendId id;

    @MapsId("userId1")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id_1", nullable = false)
    private UthUser userId1;

    @MapsId("userId2")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id_2", nullable = false)
    private UthUser userId2;

}
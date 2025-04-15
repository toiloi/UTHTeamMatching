package org.example.uthteammatching.services;

import org.example.uthteammatching.models.ListFriend;
import org.example.uthteammatching.models.ListFriendId;
import org.example.uthteammatching.models.UthUser;
import org.example.uthteammatching.repositories.ListFriendRepository;
import org.example.uthteammatching.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ListFriendService {
    @Autowired
    private ListFriendRepository listFriendRepository;


    public void createListFriend(UthUser user1, UthUser user2) {

        ListFriendId listFriendId = new ListFriendId(user1.getMaSo(), user2.getMaSo());
        ListFriend listFriend = new ListFriend(listFriendId, user1, user2);

        // Lưu vào cơ sở dữ liệu
        listFriendRepository.save(listFriend);
    }
}

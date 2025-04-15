package org.example.uthteammatching.controllers;

import lombok.RequiredArgsConstructor;
import org.example.uthteammatching.models.ChatMessage;
import org.example.uthteammatching.models.ListFriend;
import org.example.uthteammatching.models.UthUser;
import org.example.uthteammatching.repositories.ListFriendRepository;
import org.example.uthteammatching.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Controller
public class chatController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ListFriendRepository listFriendRepository;

    private UthUser addCurrentUserToModel(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getPrincipal())) {
            String username = authentication.getName();
            Optional<UthUser> userOptional = userRepository.findByUsername(username);
            if (userOptional.isPresent()) {
                UthUser currentUser = userOptional.get();
                model.addAttribute("currentUser", currentUser);
                return currentUser;
            }
        }
        return null; // Trả về null nếu không tìm thấy user
    }


    // Phương thức chung để lấy danh sách bạn bè và thêm vào model
    private void addFriendUsersToModel(Model model, UthUser currentUser) {
        if (currentUser != null) {
            List<ListFriend> friends = listFriendRepository.findByUserId1OrUserId2WithFetch(currentUser);
            List<UthUser> friendUsers = new ArrayList<>();
            for (ListFriend friend : friends) {
                if (friend.getUserId1().equals(currentUser)) {
                    friendUsers.add(friend.getUserId2());
                } else {
                    friendUsers.add(friend.getUserId1());
                }
            }
            model.addAttribute("friendUsers", friendUsers);
        }
    }

    @GetMapping("/chat")
    public String chatPage(@RequestParam("friendId") Long friendId, Model model) {
        UthUser currentUser = addCurrentUserToModel(model);
        addFriendUsersToModel(model, currentUser);
        model.addAttribute("currentUser", currentUser);

        Optional<UthUser> friend = userRepository.findById(friendId);

        if(friend.isPresent()) {
            UthUser friendUser = friend.get();
            model.addAttribute("friend", friendUser);
        }
        return "chat";
    }


    @MessageMapping("/chat")
    public void sendChatMessage(ChatMessage message, Principal principal) {
        System.out.println("Principal name: " + principal.getName());
        System.out.println("Received message: " + message.getContent());

        // Lấy thông tin người gửi
        UthUser sender = userRepository.findById(message.getSenderId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        message.setSenderName(sender.getTen());

        // Log
        System.out.println("Người gửi: " + message.getSenderId() + " " + message.getSenderName());
        System.out.println("Gửi message đến user " + message.getReceiverId() + ": " + message.getContent());

        messagingTemplate.convertAndSend(
                "/topic/messages.user-" + message.getReceiverId(),  // destination riêng của người nhận
                message
        );

        // Gửi lại cho người gửi (để họ cũng thấy tin mình vừa gửi)
        messagingTemplate.convertAndSend(
                "/topic/messages.user-" + message.getSenderId(),
                message
        );
    }



}

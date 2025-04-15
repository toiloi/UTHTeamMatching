package org.example.uthteammatching.controllers;

import lombok.RequiredArgsConstructor;
import org.example.uthteammatching.models.ChatMessage;
import org.example.uthteammatching.models.ListFriend;
import org.example.uthteammatching.models.UthUser;
import org.example.uthteammatching.repositories.ChatMessageRepository;
import org.example.uthteammatching.repositories.ListFriendRepository;
import org.example.uthteammatching.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
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
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
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

    @Autowired
    private ChatMessageRepository chatMessageRepository;


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


    @GetMapping("/getMessages")
    @ResponseBody
    public List<ChatMessage> getMessages(@RequestParam Long senderId, @RequestParam Long receiverId) {
        return chatMessageRepository.findConversation(senderId, receiverId);
    }


    @EventListener
    public void handleMessageReceived(ChatMessage message) {
        // Cập nhật trạng thái tin nhắn
        Optional<ChatMessage> chatMessage = chatMessageRepository.findById(message.getId());
        chatMessage.ifPresent(msg -> {
            msg.setStatus(ChatMessage.MessageStatus.DELIVERED);
            chatMessageRepository.save(msg);
        });
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

        message.setTimestamp(ZonedDateTime.now(ZoneOffset.UTC));
        message.setStatus(ChatMessage.MessageStatus.SENT);
        UthUser sender = userRepository.findById(message.getSenderId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        message.setSenderName(sender.getTen());
        chatMessageRepository.save(message);

        messagingTemplate.convertAndSend(
                "/topic/messages.user-" + message.getReceiverId(),
                message
        );

        messagingTemplate.convertAndSend(
                "/topic/messages.user-" + message.getSenderId(),
                message
        );
    }



}

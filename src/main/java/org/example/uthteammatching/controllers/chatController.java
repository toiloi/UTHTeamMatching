package org.example.uthteammatching.controllers;

import lombok.RequiredArgsConstructor;
import org.example.uthteammatching.models.*;
import org.example.uthteammatching.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.*;

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
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private ThanhvienProjectRepository thanhvienProjectRepository;


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

    @GetMapping("/getMessagesGroup")
    @ResponseBody
    public List<ChatMessage> getMessagesGroup(@RequestParam Long groupId) {
        return chatMessageRepository.findByGroupId(groupId);
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



    @GetMapping("/chatFriend/{friendId}")
    public String chatFriend(@PathVariable("friendId") Long friendId, Model model) {
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


    @GetMapping("/project/{projectId}")
    public String chatProject(@PathVariable("projectId") Long projectId, Model model) {
        UthUser currentUser = addCurrentUserToModel(model);
        addFriendUsersToModel(model, currentUser);
        model.addAttribute("currentUser", currentUser);
        Project project = projectRepository.findByMaProject(projectId);
        model.addAttribute("project", project);
        List<ThanhvienProject> members = thanhvienProjectRepository.findByProjectMaSo(project);
        model.addAttribute("members", members);
        return "project-details";
    }

    @PostMapping("/api/projects/{id}/evaluate-and-complete")
    @ResponseBody
    @Transactional
    public Map<String, Object> evaluateAndCompleteProject(@PathVariable Long id, @RequestBody Map<String, Object> evaluationData) {
        Map<String, Object> response = new HashMap<>();
        try {
            Project project = projectRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Dự án không tồn tại"));

            // Lưu điểm và nhận xét vào Project
            Double diem = Double.valueOf(evaluationData.get("diem").toString());
            String nhanXet = evaluationData.get("nhanXet").toString();

            // Chuyển đổi điểm từ thang 0-10 sang Integer (có thể nhân lên nếu cần)
            project.setDiem((int) Math.round(diem));
            project.setNhanXet(nhanXet);

            // Cập nhật trạng thái thành "Hoàn thành"
            project.setTrangThai("Hoàn thành");

            projectRepository.save(project);

            response.put("success", true);
            response.put("message", "Đánh giá và cập nhật trạng thái dự án thành công!");
            return response;
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", "Lỗi khi đánh giá và cập nhật trạng thái: " + e.getMessage());
            return response;
        }
    }



    @MessageMapping("/chat")
    public void sendChatMessage(ChatMessage message) {

        message.setTimestamp(ZonedDateTime.now(ZoneOffset.UTC));
        message.setStatus(ChatMessage.MessageStatus.SENT);
        UthUser sender = userRepository.findById(message.getSenderId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        message.setSenderName(sender.getTen());
        chatMessageRepository.save(message);

        if (message.getGroupId() != null) {
            messagingTemplate.convertAndSend(
                    "/topic/messages.group-" + message.getGroupId(),
                    message
            );
        } else {
            // Chat 1-1
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



}
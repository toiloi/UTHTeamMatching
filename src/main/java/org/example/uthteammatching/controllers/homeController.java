package org.example.uthteammatching.controllers;

import org.example.uthteammatching.models.BaiViet;
import org.example.uthteammatching.models.ListFriend;
import org.example.uthteammatching.models.UthUser;
import org.example.uthteammatching.repositories.ListFriendRepository;
import org.example.uthteammatching.repositories.UserRepository;
import org.example.uthteammatching.services.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class homeController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ListFriendRepository listFriendRepository;

    @Autowired
    private ArticleService articleService;

    // Phương thức chung để lấy currentUser và thêm vào model
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

    @GetMapping("/")
    public String home(Model model) {
        UthUser currentUser = addCurrentUserToModel(model);
        addFriendUsersToModel(model, currentUser); // Thêm danh sách bạn bè
        if (currentUser != null) {
            List<BaiViet> baiViets = articleService.getAllArticles();
            model.addAttribute("baiViets", baiViets);
        }
        return "home"; // Trả về home.html
    }

    @GetMapping("/project")
    public String project(Model model) {
        UthUser currentUser = addCurrentUserToModel(model);
        addFriendUsersToModel(model, currentUser); // Thêm danh sách bạn bè
        return "project";
    }

    @GetMapping("/InfoUser/{id}")
    public String showProfile(@PathVariable("id") Long id, Model model) {
        List<BaiViet> baiViets = articleService.getAllArticles();
        UthUser user = null;
        for (BaiViet bv : baiViets) {
            if (bv.getUserMaSo().getMaSo().equals(id)) {
                user = bv.getUserMaSo();
                break;
            }
        }
        if (user != null) {
            model.addAttribute("user", user);
            return "InfoUser";
        } else {
            model.addAttribute("errorMessage", "User not found");
            return "404";
        }
    }

    @GetMapping("/user-detail/{id}")
    public String userDetail(@PathVariable("id") Long id, Model model) {
        Optional<UthUser> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            UthUser user = userOptional.get();
            model.addAttribute("student", user); // Using "student" to match the template
            UthUser currentUser = addCurrentUserToModel(model);
            addFriendUsersToModel(model, currentUser);
            return "user-detail";
        } else {
            model.addAttribute("errorMessage", "User not found");
            return "404";
        }
    }
    @PostMapping("/user-detail/{id}")
    public String updateUser(@PathVariable("id") Long id, UthUser updatedUser, Model model) {
        Optional<UthUser> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            UthUser user = userOptional.get();
            model.addAttribute("student", user);
            user.setHo(updatedUser.getHo());
            user.setTen(updatedUser.getTen());

            user.setEmail(updatedUser.getEmail());
            user.setSdt(updatedUser.getSdt());
            user.setGioiTinh(updatedUser.getGioiTinh());
            user.setChuyenNganh(updatedUser.getChuyenNganh());
            user.setAvatar(updatedUser.getAvatar());

            userRepository.save(user); // Lưu vào database

            return "redirect:/user-detail/" + id; // Quay lại trang chi tiết
        } else {
            model.addAttribute("errorMessage", "User not found");
            return "404";
        }
    }
}


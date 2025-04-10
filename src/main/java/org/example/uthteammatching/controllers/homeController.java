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

    @GetMapping("/")
    public String home(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getPrincipal())) {
            String username = authentication.getName();
            Optional<UthUser> userOptional = userRepository.findByUsername(username);
            if (userOptional.isPresent()) {
                UthUser currentUser = userOptional.get(); // Khai báo currentUser trong phạm vi này
                List<BaiViet> baiViets = articleService.getAllArticles();
                model.addAttribute("baiViets", baiViets);
                model.addAttribute("currentUser", currentUser);

                // Lấy danh sách bạn bè của currentUser
                List<ListFriend> friends = listFriendRepository.findByUserId1OrUserId2(currentUser, currentUser);
                model.addAttribute("friends", friends);
            }
        }
        return "home"; // Trả về home.html
    }

    @GetMapping("/project")
    public String project(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getPrincipal())) {
            String username = authentication.getName();
            Optional<UthUser> userOptional = userRepository.findByUsername(username);
            if (userOptional.isPresent()) {
                UthUser currentUser = userOptional.get();
                model.addAttribute("currentUser", currentUser);
            }
        }
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
}
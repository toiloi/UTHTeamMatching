package org.example.uthteammatching.controllers;

import org.example.uthteammatching.models.UthUser;
import org.example.uthteammatching.repositories.UserRepository; // Sử dụng UserRepository thay vì UthUserRepository
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;

@Controller
public class homeController {

    @Autowired
    private UserRepository userRepository; // Inject UserRepository

    @GetMapping("/")
    public String home(Model model) {
        // Lấy thông tin người dùng đã đăng nhập từ SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Kiểm tra xem người dùng đã đăng nhập chưa
        if (authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getPrincipal())) {
            // Lấy username của người dùng đã đăng nhập
            String username = authentication.getName();

            // Truy vấn UthUser từ database dựa trên username
            Optional<UthUser> userOptional = userRepository.findByUsername(username);

            // Nếu tìm thấy user, thêm vào model
            if (userOptional.isPresent()) {
                UthUser currentUser = userOptional.get();
                model.addAttribute("currentUser", currentUser);
            } else {
                // Trường hợp không tìm thấy user (ít xảy ra nếu Security hoạt động đúng)
                model.addAttribute("currentUser", null);
            }
        }

        return "home"; // Trả về template home.html
    }
}
package org.example.uthteammatching.controllers;

import org.example.uthteammatching.models.Role;
import org.example.uthteammatching.models.UthUser;
import org.example.uthteammatching.models.UserRole;
import org.example.uthteammatching.repositories.RoleRepository;
import org.example.uthteammatching.repositories.UserRepository;
import org.example.uthteammatching.repositories.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.security.core.Authentication;

@Controller
public class accountController {
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @GetMapping("/account")
    public String showAccountForm() {
        return "account";
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam String username,
                             @RequestParam String pass,
                             @RequestParam String email,
                             @RequestParam String ho,
                             @RequestParam String ten,
                             @RequestParam String sdt,
                             @RequestParam String gioiTinh,
                             @RequestParam(required = false) String chuyenNganh,
                             RedirectAttributes redirectAttributes) {
        // Kiểm tra username đã tồn tại chưa
        if (userRepository.existsByUsername(username)) {
            redirectAttributes.addFlashAttribute("error", "Tên đăng nhập đã tồn tại!");
            return "redirect:/account";
        }

        // Kiểm tra email đã tồn tại chưa
        if (userRepository.existsByEmail(email)) {
            redirectAttributes.addFlashAttribute("error", "Email đã được sử dụng!");
            return "redirect:/account";
        }

        // Tạo user mới
        UthUser user = new UthUser();
        user.setUsername(username);
        user.setPass(passwordEncoder.encode(pass)); // Mã hóa mật khẩu
        user.setEmail(email);
        user.setHo(ho);
        user.setTen(ten);
        user.setSdt(sdt);
        user.setGioiTinh(gioiTinh);
        user.setChuyenNganh(chuyenNganh);
        user.setEnabled(true); // Mặc định là active

        // Lưu user vào database
        userRepository.save(user);

        // Tìm hoặc tạo role USER
        Role userRole = roleRepository.findByTen("USER")
                .orElseGet(() -> {
                    Role newRole = new Role();
                    newRole.setTen("USER");
                    return roleRepository.save(newRole);
                });

        // Tạo UserRole và lưu vào database
        UserRole userRoleEntity = new UserRole();
        userRoleEntity.setUser(user);
        userRoleEntity.setRole(userRole);
        userRoleRepository.save(userRoleEntity);

        redirectAttributes.addFlashAttribute("success", "Đăng ký thành công!");
        return "redirect:/account";
    }

    @PostMapping("/account/login")
    public String loginUser(@RequestParam String username,
                          @RequestParam String password,
                          RedirectAttributes redirectAttributes) {
        try {
            UthUser user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            
            // Kiểm tra xem user có role ADMIN không
            boolean isAdmin = user.getUserRoles().stream()
                    .anyMatch(userRole -> userRole.getRole().getTen().equals("ADMIN"));
            
            if (isAdmin) {
                redirectAttributes.addFlashAttribute("error", "Tài khoản admin không thể đăng nhập ở đây");
                return "redirect:/account";
            }
            
            return "redirect:/home";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Tên đăng nhập hoặc mật khẩu không đúng");
            return "redirect:/account";
        }
    }

    @GetMapping("/account/logout")
    public String logout() {
        return "redirect:/account?logout";
    }

    @GetMapping("/login-success")
    public String loginSuccess(Authentication authentication) {
        if (authentication != null) {
            UthUser user = userRepository.findByUsername(authentication.getName())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            
            // Kiểm tra xem user có role ADMIN không
            boolean isAdmin = user.getUserRoles().stream()
                    .anyMatch(userRole -> userRole.getRole().getTen().equals("ADMIN"));
            
            if (isAdmin) {
                return "redirect:/admin";
            }
        }
        return "redirect:/home";
    }
} 
package org.example.uthteammatching.controllers;

import org.example.uthteammatching.models.UthUser;
import org.example.uthteammatching.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class accountController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/account")
    public String showLoginForm() {
        return "account";
    }

    @PostMapping("/register")
    public String registerUser(
            @RequestParam("username") String username,
            @RequestParam("pass") String pass,
            @RequestParam("ho") String ho,
            @RequestParam("ten") String ten,
            @RequestParam("gioiTinh") String gioiTinh,
            @RequestParam("chuyenNganh") String chuyenNganh,
            @RequestParam("email") String email,
            @RequestParam("sdt") String sdt,
            RedirectAttributes redirectAttributes) {
        
        // Kiểm tra username đã tồn tại chưa
        if (userRepository.findByUsername(username).isPresent()) {
            redirectAttributes.addFlashAttribute("error", "Tên đăng nhập đã tồn tại!");
            return "redirect:/account";
        }

        // Tạo mã số tự động (tạm thời dùng username)
        String maSo = username;

        // Tạo user mới
        UthUser user = new UthUser();
        user.setMaSo(maSo);
        user.setUsername(username);
        user.setPass(pass);
        user.setHo(ho);
        user.setTen(ten);
        user.setGioiTinh(gioiTinh);
        user.setChuyenNganh(chuyenNganh);
        user.setEmail(email);
        user.setSdt(sdt);
        user.setRoleId((short) 1); // 1: User thường

        // Lưu user
        userRepository.save(user);

        redirectAttributes.addFlashAttribute("success", "Đăng ký thành công! Vui lòng đăng nhập.");
        return "redirect:/account";
    }
}
package org.example.uthteammatching.controllers;

import org.example.uthteammatching.models.UthUser;
import org.example.uthteammatching.repositories.UserRepository;
import org.example.uthteammatching.repositories.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class adminController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @GetMapping
    public String showAdminPanel() {
        return "admin/admin";
    }

    @GetMapping("/users")
    public String showUserManagement(Model model) {
        List<UthUser> users = userRepository.findAll();
        model.addAttribute("users", users);
        return "admin/users";
    }

    @GetMapping("/api/admin/users/available")
    @ResponseBody
    public List<UthUser> getAvailableUsers() {
        return userRepository.findAll().stream()
                .filter(user -> userRoleRepository.findByUserMaSo(user.getMaSo()).isEmpty())
                .collect(Collectors.toList());
    }

    @GetMapping("/api/admin/users/available/search")
    @ResponseBody
    public List<UthUser> searchAvailableUsers(@RequestParam String term) {
        return userRepository.findAll().stream()
                .filter(user -> userRoleRepository.findByUserMaSo(user.getMaSo()).isEmpty())
                .filter(user -> 
                    user.getTen().toLowerCase().contains(term.toLowerCase()) ||
                    user.getHo().toLowerCase().contains(term.toLowerCase()) ||
                    user.getEmail().toLowerCase().contains(term.toLowerCase()))
                .collect(Collectors.toList());
    }
}

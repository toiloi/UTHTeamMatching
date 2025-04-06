package org.example.uthteammatching.controllers;

import org.example.uthteammatching.models.Role;
import org.example.uthteammatching.models.UthUser;
import org.example.uthteammatching.models.UserRole;
import org.example.uthteammatching.repositories.RoleRepository;
import org.example.uthteammatching.repositories.UserRepository;
import org.example.uthteammatching.repositories.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin/users")
@PreAuthorize("hasRole('ADMIN')")
public class AdminUserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @GetMapping
    public List<UthUser> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/{userId}")
    public UthUser getUser(@PathVariable Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @GetMapping("/search")
    public List<UthUser> searchUsers(@RequestParam String term) {
        return userRepository.findByHoContainingOrTenContainingOrEmailContaining(
                term, term, term);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<?> updateUser(
            @PathVariable Long userId,
            @RequestBody Map<String, Object> userData) {
        UthUser user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Update user data
        user.setHo((String) userData.get("ho"));
        user.setTen((String) userData.get("ten"));
        user.setEmail((String) userData.get("email"));
        user.setSdt((String) userData.get("sdt"));
        user.setChuyenNganh((String) userData.get("chuyenNganh"));
        user.setGioiTinh((String) userData.get("gioiTinh"));
        user.setEnabled((Boolean) userData.get("enabled"));

        // Update roles
        @SuppressWarnings("unchecked")
        List<Long> roleIds = (List<Long>) userData.get("roles");
        
        // Remove old roles
        userRoleRepository.deleteByUserMaSo(userId);
        
        // Add new roles
        for (Long roleId : roleIds) {
            Role role = roleRepository.findById(roleId)
                    .orElseThrow(() -> new RuntimeException("Role not found"));
            
            UserRole userRole = new UserRole();
            userRole.setUser(user);
            userRole.setRole(role);
            userRoleRepository.save(userRole);
        }

        userRepository.save(user);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId) {
        // Delete user roles first
        userRoleRepository.deleteByUserMaSo(userId);
        
        // Then delete user
        userRepository.deleteById(userId);
        return ResponseEntity.ok().build();
    }
} 
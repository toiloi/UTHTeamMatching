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
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin/roles")
@PreAuthorize("hasRole('ADMIN')")
public class AdminRoleController {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @GetMapping
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<?> createRole(@RequestBody Role role) {
        roleRepository.save(role);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{roleId}")
    public ResponseEntity<?> deleteRole(@PathVariable Long roleId) {
        roleRepository.deleteById(roleId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{roleId}/users")
    public List<UthUser> getUsersInRole(@PathVariable Long roleId) {
        return userRoleRepository.findByRoleMaSo(roleId)
                .stream()
                .map(UserRole::getUser)
                .collect(Collectors.toList());
    }

    @GetMapping("/{roleId}/users/search")
    public List<UthUser> searchUsersInRole(
            @PathVariable Long roleId,
            @RequestParam String term) {
        return userRoleRepository.findByRoleMaSo(roleId)
                .stream()
                .map(UserRole::getUser)
                .filter(user -> 
                    user.getTen().toLowerCase().contains(term.toLowerCase()) ||
                    user.getHo().toLowerCase().contains(term.toLowerCase()) ||
                    user.getEmail().toLowerCase().contains(term.toLowerCase()))
                .collect(Collectors.toList());
    }

    @PostMapping("/{roleId}/users")
    public ResponseEntity<?> addUsersToRole(
            @PathVariable Long roleId,
            @RequestBody List<Long> userIds) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        for (Long userId : userIds) {
            UthUser user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            if (!userRoleRepository.existsByUserMaSoAndRoleMaSo(userId, roleId)) {
                UserRole userRole = new UserRole();
                userRole.setUser(user);
                userRole.setRole(role);
                userRoleRepository.save(userRole);
            }
        }

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{roleId}/users/{userId}")
    public ResponseEntity<?> removeUserFromRole(
            @PathVariable Long roleId,
            @PathVariable Long userId) {
        userRoleRepository.deleteByUserMaSoAndRoleMaSo(userId, roleId);
        return ResponseEntity.ok().build();
    }
} 
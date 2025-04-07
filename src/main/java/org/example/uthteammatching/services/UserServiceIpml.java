package org.example.uthteammatching.services;

import org.example.uthteammatching.models.Role;
import org.example.uthteammatching.models.UserRole;
import org.example.uthteammatching.models.UthUser;
import org.example.uthteammatching.dto.RegisterRequest;
import org.example.uthteammatching.repositories.RoleRepository;
import org.example.uthteammatching.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserServiceIpml implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public UthUser findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public UthUser authenticate(String username, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UthUser user = findByUsername(username);

        return user;
    }

    public String registerUser(RegisterRequest registerRequest) {

        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            return "Tên đăng nhập đã tồn tại.";
        }

        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            return "Email đã tồn tại.";
        }


        String encodedPassword = passwordEncoder.encode(registerRequest.getPassword());

        UthUser user = new UthUser();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setHo(registerRequest.getHo());
        user.setTen(registerRequest.getTen());
        user.setGioiTinh(registerRequest.getGioiTinh());
        user.setChuyenNganh(registerRequest.getChuyenNganh());
        user.setSdt(registerRequest.getSdt());
        user.setPass(encodedPassword);
        user.setEnabled(true);

        Role role = roleRepository.findByTen("USER");
        if (role == null) {
            return "Không tìm thấy vai trò người dùng.";
        }

        Set<UserRole> userRoles = new HashSet<>();
        UserRole userRole = new UserRole();
        userRole.setUser(user);
        userRole.setRole(role);
        userRoles.add(userRole);
        user.setUserRoles(userRoles);

        userRepository.save(user);

        return "Đăng ký thành công.";
    }

}

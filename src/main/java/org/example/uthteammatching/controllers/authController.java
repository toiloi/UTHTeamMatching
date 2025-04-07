package org.example.uthteammatching.controllers;

import org.example.uthteammatching.dto.LoginRequest;
import org.example.uthteammatching.dto.RegisterRequest;
import org.example.uthteammatching.dto.UserDTO;
import org.example.uthteammatching.jwt.JwtService;
import org.example.uthteammatching.models.UthUser;
import org.example.uthteammatching.services.UserServiceIpml;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/auth")
public class authController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService tokenProvider;

    @Autowired
    private UserServiceIpml userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {

        UthUser user = userService.authenticate(loginRequest.getUsername(), loginRequest.getPassword());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Thông tin đăng nhập không hợp lệ");
        }

        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(user.getUsername());
        userDTO.setHo(user.getHo());
        userDTO.setTen(user.getTen());
        userDTO.setEmail(user.getEmail());

        Long roleId = user.getUserRoles().stream()
                .map(userRole -> userRole.getRole().getMaSo())
                .findFirst()
                .orElse(null);
        userDTO.setRoleId(roleId);


        return ResponseEntity.ok(userDTO);
    }


    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
        return ResponseEntity.ok("Đăng ký thành công");
    }
}

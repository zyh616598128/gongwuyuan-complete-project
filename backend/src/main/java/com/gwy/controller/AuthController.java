package com.gwy.controller;

import com.gwy.dto.ApiResponse;
import com.gwy.dto.LoginRequest;
import com.gwy.dto.RegisterRequest;
import com.gwy.model.User;
import com.gwy.service.UserService;
import com.gwy.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<String>> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            // Directly verify the user credentials against the database
            User user = userService.findByUsername(loginRequest.getUsername()).orElse(null);
            
            if (user == null) {
                return ResponseEntity.ok(ApiResponse.error("用户名或密码错误"));
            }
            
            if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
                return ResponseEntity.ok(ApiResponse.error("用户名或密码错误"));
            }

            // Generate JWT token manually
            String jwt = jwtUtil.generateToken(loginRequest.getUsername());

            return ResponseEntity.ok(ApiResponse.success("登录成功", jwt));
        } catch (Exception e) {
            // Log the exception for debugging (in a real app, use proper logging)
            System.out.println("Login error: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.ok(ApiResponse.error("登录时发生错误"));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<String>> register(@Valid @RequestBody RegisterRequest registerRequest) {
        if (userService.existsByUsername(registerRequest.getUsername())) {
            return ResponseEntity.ok(ApiResponse.error("用户名已存在"));
        }

        if (userService.existsByEmail(registerRequest.getEmail())) {
            return ResponseEntity.ok(ApiResponse.error("邮箱已被注册"));
        }

        User user = userService.register(registerRequest);

        return ResponseEntity.ok(ApiResponse.success("注册成功"));
    }
}
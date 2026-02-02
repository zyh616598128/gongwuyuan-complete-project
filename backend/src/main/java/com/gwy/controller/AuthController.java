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

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<String>> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            String jwt = jwtUtil.generateToken(loginRequest.getUsername());

            return ResponseEntity.ok(ApiResponse.success("登录成功", jwt));
        } catch (Exception e) {
            return ResponseEntity.ok(ApiResponse.error("用户名或密码错误"));
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
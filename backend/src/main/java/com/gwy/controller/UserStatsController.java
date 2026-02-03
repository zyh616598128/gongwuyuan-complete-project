package com.gwy.controller;

import com.gwy.dto.ApiResponse;
import com.gwy.model.User;
import com.gwy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.gwy.dto.UpdateProfileRequest;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserStatsController {

    @Autowired
    private UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getUserProfile() {
        try {
            // Get the authenticated user
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            
            User user = userService.findByUsername(username).orElse(null);
            
            if (user == null) {
                return ResponseEntity.ok(ApiResponse.error("用户不存在"));
            }

            Map<String, Object> profileData = new HashMap<>();
            profileData.put("id", user.getId());
            profileData.put("username", user.getUsername());
            profileData.put("nickname", user.getNickname());
            profileData.put("email", user.getEmail());
            profileData.put("phone", user.getPhone());
            profileData.put("createTime", user.getCreateTime());
            profileData.put("studyDays", userService.calculateStudyDays(user.getId()));
            profileData.put("totalQuestions", userService.getTotalQuestionsAnswered(user.getId()));
            profileData.put("answeredQuestions", userService.getAnsweredQuestionsCount(user.getId()));
            profileData.put("accuracyRate", userService.getAccuracyRate(user.getId()));
            
            return ResponseEntity.ok(ApiResponse.success("获取用户资料成功", profileData));
        } catch (Exception e) {
            return ResponseEntity.ok(ApiResponse.error("获取用户资料失败: " + e.getMessage()));
        }
    }
    
    @PutMapping("/profile")
    public ResponseEntity<ApiResponse<Object>> updateUserProfile(@Valid @RequestBody UpdateProfileRequest request) {
        try {
            // Get the authenticated user
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            
            User user = userService.findByUsername(username).orElse(null);
            
            if (user == null) {
                return ResponseEntity.ok(ApiResponse.error("用户不存在"));
            }

            // 更新可修改的信息
            if (request.getNickname() != null) {
                user.setNickname(request.getNickname());
            }
            
            if (request.getPhone() != null) {
                user.setPhone(request.getPhone());
            }

            // 保存更新后的用户信息
            User updatedUser = userService.update(user);

            Map<String, Object> profileData = new HashMap<>();
            profileData.put("id", updatedUser.getId());
            profileData.put("username", updatedUser.getUsername());
            profileData.put("nickname", updatedUser.getNickname());
            profileData.put("email", updatedUser.getEmail());
            profileData.put("phone", updatedUser.getPhone());
            profileData.put("createTime", updatedUser.getCreateTime());
            
            return ResponseEntity.ok(ApiResponse.success("用户资料更新成功", profileData));
        } catch (Exception e) {
            return ResponseEntity.ok(ApiResponse.error("更新用户资料失败: " + e.getMessage()));
        }
    }
}
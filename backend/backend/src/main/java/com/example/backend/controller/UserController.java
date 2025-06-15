package com.example.backend.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.backend.dto.request.LoginRequest;
import com.example.backend.dto.request.RegisterRequest;
import com.example.backend.dto.response.LoginResponse;
import com.example.backend.dto.response.RegisterResponse;
import com.example.backend.dto.response.UserProfileResponse;
import com.example.backend.model.User;
import com.example.backend.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserController {

  private final UserService userService;

  // 注册
  @PostMapping("/register")
  public ResponseEntity<RegisterResponse> register(@RequestBody @Valid RegisterRequest request) {
    return ResponseEntity.ok().body(userService.register(request));
  }

  // 登录
  @PostMapping("/login")
  public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest request) {
    return ResponseEntity.ok(userService.login(request));
  }

  // @GetMapping("/current")
  // public ResponseEntity<UserProfileResponse> getCurrentUser(
  // @RequestHeader("Authorization") String token) {
  // String username = jwtUtils.getUsernameFromToken(token);
  // return ResponseEntity.ok(userService.getUserProfileByUsername(username));
  // }

  // 获取当前用户信息
  @GetMapping("/current")
  public ResponseEntity<UserProfileResponse> getCurrentUser(
      @AuthenticationPrincipal UserDetails userDetails) {
    return ResponseEntity.ok(userService.getUserProfileByUsername(userDetails.getUsername()));
  }

  // 删除当前用户
  @DeleteMapping("/current")
  public ResponseEntity<?> deleteUser(@AuthenticationPrincipal UserDetails userDetails) {
    userService.deleteUserByUsername(userDetails.getUsername());
    return ResponseEntity.noContent().build();
  }

  // 根据 ID 获取用户信息
  @GetMapping("/{id}")
  public UserProfileResponse getUserById(@PathVariable Long id) {
    return userService.getUserProfileById(id);
  }

  // 获取所有用户
  @GetMapping
  public ResponseEntity<List<User>> getAllUsers() {
    return ResponseEntity.ok(userService.getAllUsers());
  }
}

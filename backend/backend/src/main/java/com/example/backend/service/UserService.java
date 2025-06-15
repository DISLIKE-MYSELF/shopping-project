package com.example.backend.service;

import java.util.List;
import com.example.backend.dto.response.LoginResponse;
import com.example.backend.dto.response.UserProfileResponse;
import com.example.backend.model.User;

public interface UserService {


  List<User> getAllUsers();

  boolean existsByUsername(String username);

  User getUserByUsername(String username);

  User getUserById(Long id);

  User getUserByEmail(String email);

  UserProfileResponse getUserProfileById(Long userId);

  UserProfileResponse getUserProfileByUsername(String username);

  User register(User user);

  LoginResponse login(String username, String password);

  void deleteUserByUsername(String username);
}

package com.example.backend.service;

import java.util.List;
import com.example.backend.dto.request.LoginRequest;
import com.example.backend.dto.request.RegisterRequest;
import com.example.backend.dto.response.LoginResponse;
import com.example.backend.dto.response.RegisterResponse;
import com.example.backend.dto.response.UserProfileResponse;
import com.example.backend.model.User;

public interface UserService {


  List<User> getAllUsers();

  User getUserByUsername(String username);

  UserProfileResponse getUserProfileById(Long id);

  UserProfileResponse getUserProfileByUsername(String username);

  RegisterResponse register(RegisterRequest request);

  LoginResponse login(LoginRequest request);

  void deleteUserByUsername(String username);
}

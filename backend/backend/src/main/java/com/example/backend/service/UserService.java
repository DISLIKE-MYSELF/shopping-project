package com.example.backend.service;

import com.example.backend.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> getAllUsers();
    Optional<User> getUserById(Long id);
    User registerUser(User user);
    Optional<User> login(String username, String password);
    void deleteUser(Long id);
}

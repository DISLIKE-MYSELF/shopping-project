package com.example.backend.service.impl;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import com.example.backend.exception.EntityNotFoundException;
import com.example.backend.model.User;
import com.example.backend.repository.UserRepository;
import com.example.backend.service.CustomUserDetailsService;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CustomUserDetailsServiceImpl implements CustomUserDetailsService {

  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws EntityNotFoundException {
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new EntityNotFoundException("user", username));

    return user;
  }
}

package com.example.backend.mapper;

import org.springframework.stereotype.Component;
import com.example.backend.dto.response.UserProfileResponse;
import com.example.backend.model.User;

@Component
public class UserMapper {

  public UserProfileResponse toUserProfileResponse(User user) {
    return new UserProfileResponse(user.getId(), user.getUsername(), user.getEmail(),
        user.getAddress(), user.getCreatedAt());
  }
}

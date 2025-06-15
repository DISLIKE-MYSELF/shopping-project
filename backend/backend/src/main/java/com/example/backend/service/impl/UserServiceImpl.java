package com.example.backend.service.impl;

import java.sql.Timestamp;
import java.util.List;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.backend.dto.response.LoginResponse;
import com.example.backend.dto.response.UserProfileResponse;
import com.example.backend.exception.BusinessException;
import com.example.backend.exception.EntityNotFoundException;
import com.example.backend.exception.UnauthorizedException;
import com.example.backend.mapper.UserMapper;
import com.example.backend.model.User;
import com.example.backend.repository.UserRepository;
import com.example.backend.service.UserService;
import com.example.backend.utils.JwtUtils;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final AuthenticationManager authenticationManager;
  private final JwtUtils jwtUtils;
  private final UserMapper userMapper;

  @Override
  public boolean existsByUsername(String username) {
    return userRepository.existsByUsername(username);
  }

  @Override
  public List<User> getAllUsers() {
    return userRepository.findAll();
  }

  @Override
  public User getUserById(Long id) {
    User user =
        userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User", id));
    return user;
  }

  @Override
  public User getUserByUsername(String username) {
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new EntityNotFoundException("User", username));
    return user;
  }

  @Override
  public User getUserByEmail(String email) {
    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new EntityNotFoundException("User", email));
    return user;
  }

  @Override
  public UserProfileResponse getUserProfileById(Long userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new EntityNotFoundException("User", userId));
    return userMapper.toUserProfileResponse(user);
  }

  @Override
  public UserProfileResponse getUserProfileByUsername(String username) {
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new EntityNotFoundException("User", username));
    return userMapper.toUserProfileResponse(user);
  }

  @Override
  @Transactional
  public User register(User user) {
    if (userRepository.existsByUsername(user.getUsername())) {
      throw new BusinessException("用户名已存在");
    }

    if (userRepository.existsByEmail(user.getEmail())) {
      throw new BusinessException("邮箱已存在");
    }

    user.setCreatedAt(new Timestamp(System.currentTimeMillis()));
    return userRepository.save(user);
  }

  @Override
  @Transactional
  public LoginResponse login(String username, String password) {
    if (!userRepository.existsByUsername(username)) {
      throw new EntityNotFoundException("User", username);
    }
    try {
      // 认证用户凭证
      Authentication authentication = authenticationManager
          .authenticate(new UsernamePasswordAuthenticationToken(username, password));

      // 生成JWT令牌
      UserDetails userDetails = (UserDetails) authentication.getPrincipal();
      return new LoginResponse(jwtUtils.generateToken(userDetails));

    } catch (BadCredentialsException e) {
      throw new UnauthorizedException("密码错误");
    } catch (AuthenticationException e) {
      throw new UnauthorizedException("登录失败");
    }
  }

  @Override
  public void deleteUserByUsername(String username) {
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new EntityNotFoundException("User", username));

    userRepository.deleteById(user.getId());
  }
}

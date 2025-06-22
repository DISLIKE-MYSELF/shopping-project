package com.example.backend.service.impl;

import java.util.List;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.backend.dto.request.LoginRequest;
import com.example.backend.dto.request.RegisterRequest;
import com.example.backend.dto.request.UpdateUserProfileRequest;
import com.example.backend.dto.response.LoginResponse;
import com.example.backend.dto.response.RegisterResponse;
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
  private final PasswordEncoder passwordEncoder;

  @Override
  public List<User> getAllUsers() {
    return userRepository.findAll();
  }

  @Override
  public User getUserByUsername(String username) {
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new EntityNotFoundException("User", username));
    return user;
  }

  @Override
  public UserProfileResponse getUserProfileById(Long id) {
    User user =
        userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User", id));
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
  public UserProfileResponse updateUserProfileByUsername(String username,
      UpdateUserProfileRequest request) {
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new EntityNotFoundException("User", username));
    user.setAddress(request.address());
    return userMapper.toUserProfileResponse(userRepository.saveAndFlush(user));
  }

  @Override
  @Transactional
  public RegisterResponse register(RegisterRequest request) {
    if (userRepository.existsByUsername(request.username())) {
      throw new BusinessException("用户名已存在");
    }

    if (userRepository.existsByEmail(request.email())) {
      throw new BusinessException("邮箱已存在");
    }

    User user = new User();
    user.setUsername(request.username());
    user.setEmail(request.email());
    user.setPassword(passwordEncoder.encode(request.password()));
    userRepository.save(user);
    return new RegisterResponse(user.getId());
  }

  @Override
  @Transactional
  public LoginResponse login(LoginRequest request) {
    if (!userRepository.existsByUsername(request.username())) {
      throw new EntityNotFoundException("User", request.username());
    }
    try {
      // 认证用户凭证
      Authentication authentication = authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(request.username(), request.password()));

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

package com.example.backend.service.impl;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.backend.dto.request.SendEmailRequest;
import com.example.backend.dto.response.EmailResponse;
import com.example.backend.exception.EntityNotFoundException;
import com.example.backend.exception.UnauthorizedException;
import com.example.backend.mapper.EmailMapper;
import com.example.backend.model.Email;
import com.example.backend.model.User;
import com.example.backend.repository.EmailRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.service.EmailService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

  private final EmailRepository emailRepository;
  private final UserRepository userRepository;
  private final EmailMapper emailMapper;

  @Override
  public List<EmailResponse> getEmailsByUsername(String username) {
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new EntityNotFoundException("User", username));

    List<Email> emails = emailRepository.findByUserId(user.getId());

    return emails.stream().map(emailMapper::toEmailResponse).collect(Collectors.toList());
  }

  @Override
  public EmailResponse getEmailById(String username, Long id) {
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new EntityNotFoundException("User", username));

    Email email =
        emailRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Email", id));

    if (!email.getUser().getId().equals(user.getId())) {
      throw new UnauthorizedException("无权操作");
    }

    return emailMapper.toEmailResponse(email);
  }

  @Override
  @Transactional
  public EmailResponse sendEmail(SendEmailRequest request) {
    User user = userRepository.findById(request.userId())
        .orElseThrow(() -> new EntityNotFoundException("User", request.userId()));

    Email email = new Email();
    email.setUser(user);
    email.setSubject(request.subject());
    email.setContent(request.content());

    return emailMapper.toEmailResponse(emailRepository.save(email));
  }

  @Override
  @Transactional
  public void deleteEmailById(String username, Long id) {
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new EntityNotFoundException("User", username));

    Email email =
        emailRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Email", id));

    if (!email.getUser().getId().equals(user.getId())) {
      throw new UnauthorizedException("无权操作");
    }

    emailRepository.deleteById(id);
  }
}

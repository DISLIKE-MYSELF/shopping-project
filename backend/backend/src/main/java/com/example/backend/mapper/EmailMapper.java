package com.example.backend.mapper;

import org.springframework.stereotype.Component;
import com.example.backend.dto.response.EmailResponse;
import com.example.backend.model.Email;

@Component
public class EmailMapper {
  public EmailResponse toEmailResponse(Email email) {
    return new EmailResponse(email.getId(), email.getSubject(), email.getContent(),
        email.getSentAt());
  }
}

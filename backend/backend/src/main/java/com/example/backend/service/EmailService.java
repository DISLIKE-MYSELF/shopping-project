package com.example.backend.service;

import java.util.List;
import com.example.backend.dto.request.SendEmailRequest;
import com.example.backend.dto.response.EmailResponse;

public interface EmailService {
  List<EmailResponse> getEmailsByUsername(String username);

  EmailResponse getEmailById(String username, Long id);

  EmailResponse sendEmail(SendEmailRequest request);

  void deleteEmailById(String username, Long id);
}

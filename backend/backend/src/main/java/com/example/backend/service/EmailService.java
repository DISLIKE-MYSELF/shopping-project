package com.example.backend.service;

import com.example.backend.model.Email;

import java.util.List;

public interface EmailService {
    List<Email> getAllEmails();
    List<Email> getEmailsByUserId(Long userId);
    Email sendEmail(Email email);
    void deleteEmail(Long id);
}

package com.example.backend.service.impl;

import com.example.backend.model.Email;
import com.example.backend.model.User;
import com.example.backend.repository.EmailRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.service.EmailService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailServiceImpl implements EmailService {

    private final EmailRepository emailRepository;
    private final UserRepository userRepository;

    public EmailServiceImpl(EmailRepository emailRepository, UserRepository userRepository) {
        this.emailRepository = emailRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Email> getAllEmails() {
        return emailRepository.findAll();
    }

    @Override
    public List<Email> getEmailsByUserId(Long userId) {
        return emailRepository.findByUserId(userId);
    }

    @Override
    public Email sendEmail(Email email) {
        User user = userRepository.findById(email.getUser().getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        email.setUser(user);
        return emailRepository.save(email);
    }

    @Override
    public void deleteEmail(Long id) {
        emailRepository.deleteById(id);
    }
}

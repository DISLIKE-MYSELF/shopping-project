package com.example.backend.controller;

import com.example.backend.model.Email;
import com.example.backend.service.EmailService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/emails")
public class EmailController {

    private final EmailService emailService;
    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @GetMapping
    public List<Email> getAllEmails() {
        return emailService.getAllEmails();
    }

    @GetMapping("/user/{userId}")
    public List<Email> getEmailsByUser(@PathVariable Long userId) {
        return emailService.getEmailsByUserId(userId);
    }

    @PostMapping
    public Email sendEmail(@RequestBody Email email) {
        return emailService.sendEmail(email);
    }

    @DeleteMapping("/{id}")
    public void deleteEmail(@PathVariable Long id) {
        emailService.deleteEmail(id);
    }
}

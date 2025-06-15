package com.example.backend.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.backend.dto.request.SendEmailRequest;
import com.example.backend.dto.response.EmailResponse;
import com.example.backend.service.EmailService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/emails")
@AllArgsConstructor
public class EmailController {

  private final EmailService emailService;

  // 获取登录用户的所有邮件
  @GetMapping("/my-emails")
  public ResponseEntity<List<EmailResponse>> getEmailsByUsername(
      @AuthenticationPrincipal UserDetails userDetails) {
    return ResponseEntity.ok().body(emailService.getEmailsByUsername(userDetails.getUsername()));
  }

  // 获取指定邮件
  @GetMapping("/{emailId}")
  public ResponseEntity<EmailResponse> getEmailById(
      @AuthenticationPrincipal UserDetails userDetails, @PathVariable Long emailId) {
    return ResponseEntity.ok().body(emailService.getEmailById(userDetails.getUsername(), emailId));
  }

  // 发送邮件
  @PostMapping
  public ResponseEntity<EmailResponse> sendEmail(@RequestBody @Valid SendEmailRequest request) {
    return ResponseEntity.ok().body(emailService.sendEmail(request));
  }

  // 删除邮件
  @DeleteMapping("/{emailId}")
  public ResponseEntity<?> deleteEmail(@AuthenticationPrincipal UserDetails userDetails,
      @PathVariable Long emailId) {
    emailService.deleteEmailById(userDetails.getUsername(), emailId);
    return ResponseEntity.noContent().build();
  }
}

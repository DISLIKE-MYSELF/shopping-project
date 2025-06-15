package com.example.backend.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.backend.model.Email;

public interface EmailRepository extends JpaRepository<Email, Long> {
  List<Email> findByUserId(Long userId);
}

package com.example.backend.repository;

import com.example.backend.model.Email;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmailRepository extends JpaRepository<Email, Long> {
    List<Email> findByUserId(Long userId);
}

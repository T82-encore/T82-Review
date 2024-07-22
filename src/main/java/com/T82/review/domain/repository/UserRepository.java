package com.T82.review.domain.repository;

import com.T82.review.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    User findByUserId(UUID userId);
}

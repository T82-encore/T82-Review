package com.T82.review.kafka.dto.request;

import com.T82.review.domain.entity.User;

import java.util.UUID;

public record KafkaUserRequest(
        UUID userId,
        String email
) {
    public User toEntity(KafkaUserRequest kafkaUserRequest) {
        return User.builder()
                .userId(kafkaUserRequest.userId)
                .email(kafkaUserRequest.email)
                .isDeleted(false)
                .build();
    }
}

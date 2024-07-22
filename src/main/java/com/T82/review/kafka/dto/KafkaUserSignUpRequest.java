package com.T82.review.kafka.dto;

import com.T82.review.domain.entity.User;

import java.util.UUID;

public record KafkaUserSignUpRequest(
        UUID userId,
        String email,
        boolean isDeleted
) {
    public User toEntity(KafkaUserSignUpRequest kafkaUserSignUpRequest) {
        return User.builder()
                .userId(kafkaUserSignUpRequest.userId)
                .email(kafkaUserSignUpRequest.email)
                .isDeleted(kafkaUserSignUpRequest.isDeleted)
                .build();
    }
}

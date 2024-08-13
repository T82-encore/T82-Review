package com.T82.review.kafka.dto.request;

import com.T82.review.domain.entity.User;

import java.util.UUID;

public record KafkaUserRequest(
        UUID userId,
        String email,
        String name,
        boolean isArtist,
        String profileUrl
) {
    public User toEntity(KafkaUserRequest kafkaUserRequest) {
        return User.builder()
                .userId(kafkaUserRequest.userId)
                .username(kafkaUserRequest.name)
                .email(kafkaUserRequest.email)
                .isArtist(kafkaUserRequest.isArtist)
                .ImageUrl(kafkaUserRequest.profileUrl())
                .isDeleted(false)
                .build();
    }
}

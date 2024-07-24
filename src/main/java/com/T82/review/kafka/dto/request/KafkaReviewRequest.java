package com.T82.review.kafka.dto.request;

public record KafkaReviewRequest(
        Long eventInfoId,
        Double rating
) {
}
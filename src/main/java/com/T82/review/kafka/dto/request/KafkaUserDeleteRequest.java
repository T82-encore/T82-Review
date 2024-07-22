package com.T82.review.kafka.dto.request;

import java.util.UUID;

public record KafkaUserDeleteRequest(
        UUID userId
) {
}

package com.T82.user.kafka.dto.request;

import java.util.UUID;

public record KafkaUserDeleteRequest(
        UUID userId
) {
}

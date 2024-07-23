package com.T82.review.kafka.dto.request;

import com.T82.review.domain.entity.EventInfo;
import com.T82.review.domain.entity.User;

public record KafkaEventCreateRequest(
        Long eventInfoId
) {
    public EventInfo toEntity(KafkaEventCreateRequest kafkaEventCreateRequest) {
        return EventInfo.builder()
                .eventInfoId(kafkaEventCreateRequest.eventInfoId())
                .isDeleted(false)
                .build();
    }
}

package com.T82.review.kafka.dto.request;

import com.T82.review.domain.entity.EventInfo;

public record KafkaEventInfoRequest(
        Long eventInfoId

) {
    public EventInfo toEntity() {
        return EventInfo.builder()
                .eventInfoId(this.eventInfoId)
                .isDeleted(false)
                .build();
    }
}

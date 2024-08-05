package com.T82.review.kafka.dto.request;

import com.T82.review.domain.entity.EventInfo;
import com.T82.review.domain.entity.User;

import java.util.UUID;

public record KafkaEventInfoRequest(
        Long eventInfoId

) {
//    public EventInfo toEntity(KafkaEventInfoRequest kafkaEventInfoRequest) {
//        return EventInfo.builder()
//                .eventInfoId(kafkaEventInfoRequest.eventInfoId)
//                .isDeleted(false)
//                .build();
//    }
    public EventInfo toEntity() {
        return EventInfo.builder()
                .eventInfoId(this.eventInfoId)
                .isDeleted(false)
                .build();
    }
}

package com.T82.review.kafka.dto;

public record KafkaStatus<T>(
        T data, String status
) {
}


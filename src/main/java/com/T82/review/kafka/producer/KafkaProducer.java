package com.T82.review.kafka.producer;

import com.T82.review.kafka.dto.KafkaStatus;
import com.T82.review.kafka.dto.request.KafkaReviewRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaProducer {

    private final KafkaTemplate<String, KafkaStatus<KafkaReviewRequest>> kafkaReviewTemplate;

    public void createReview(KafkaReviewRequest kafkaReviewRequest, String topic) {
        KafkaStatus<KafkaReviewRequest> kafkaStatus = new KafkaStatus<>(kafkaReviewRequest,"create");
        kafkaReviewTemplate.send(topic, kafkaStatus);
    }

    public void deleteReview(KafkaReviewRequest kafkaReviewRequest, String topic) {
        KafkaStatus<KafkaReviewRequest> kafkaStatus = new KafkaStatus<>(kafkaReviewRequest,"delete");
        kafkaReviewTemplate.send(topic, kafkaStatus);
    }
}

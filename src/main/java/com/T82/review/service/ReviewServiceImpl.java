package com.T82.review.service;

import com.T82.review.domain.dto.request.AddReviewRequest;
import com.T82.review.domain.dto.response.ReviewResponse;
import com.T82.review.domain.entity.EventInfo;
import com.T82.review.domain.entity.Review;
import com.T82.review.domain.entity.User;
import com.T82.review.domain.repository.EventInfoRepository;
import com.T82.review.domain.repository.ReviewRepository;
import com.T82.review.domain.repository.UserRepository;
import com.T82.review.exception.DuplicateReviewException;
import com.T82.review.exception.EventDeleteException;
import com.T82.review.exception.NoReviewException;
import com.T82.review.exception.UserDeleteException;
import com.T82.review.global.utils.TokenInfo;
import com.T82.review.kafka.dto.KafkaStatus;
import com.T82.review.kafka.dto.request.*;
import com.T82.review.kafka.producer.KafkaProducer;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService{
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final EventInfoRepository eventInfoRepository;
    private final KafkaProducer kafkaProducer;


//    리뷰 생성
    @Override
    public void addReview(TokenInfo tokenInfo, AddReviewRequest addReviewRequest) {
        User user = getUser(tokenInfo);
        EventInfo eventInfo = getValidEventInfo(addReviewRequest.eventInfoId());
        checkDuplicateReview(user, eventInfo);
        reviewRepository.save(addReviewRequest.toEntity(user, eventInfo));
        KafkaReviewRequest kafkaReviewRequest = new KafkaReviewRequest(
                addReviewRequest.eventInfoId(),addReviewRequest.rating()
        );
        kafkaProducer.createReview(kafkaReviewRequest, "reviewTopic");

    }

//    모든 리뷰 가져오기
    @Override
    public List<ReviewResponse> getAllUserReview(TokenInfo tokenInfo) {
        User user = getUser(tokenInfo);
        List<Review> allByUser = reviewRepository.findAllByUserAndIsDeleted(user, false);
        return allByUser.stream().map(ReviewResponse::from).toList();
    }

//    한 이벤트에 대한 리뷰 가져오기
    @Override
    public List<ReviewResponse> getAllReview(Long eventInfoId) {
        EventInfo eventInfo = getValidEventInfo(eventInfoId);
        List<Review> allByEventInfo = reviewRepository.findAllByEventInfoAndIsDeleted(eventInfo, false);
        return allByEventInfo.stream().map(ReviewResponse::from).toList();
    }

//    리뷰 삭제하기
    @Override
    public void deleteReview(TokenInfo tokenInfo, Long reviewId) {
        User user = getUser(tokenInfo);
        Review review = getValidReview(user, reviewId);
        review.deleteReview();
        reviewRepository.save(review);
        KafkaReviewRequest kafkaReviewRequest = new KafkaReviewRequest(
                review.getEventInfo().getEventInfoId(), review.getRating()
        );
        kafkaProducer.deleteReview(kafkaReviewRequest, "reviewTopic");

    }




    private User getUser(TokenInfo tokenInfo) {
        User user = User.builder().userId(tokenInfo.id()).build();
        if(userRepository.findByUserId(user.getUserId()).getIsDeleted()){
            throw new UserDeleteException("해당 회원은 탈퇴한 회원입니다.");
        }
        return user;
    }

    private EventInfo getValidEventInfo(Long eventInfoId) {
        EventInfo eventInfo = EventInfo.builder().eventInfoId(eventInfoId).build();
        if (eventInfoRepository.findByEventInfoId(eventInfo.getEventInfoId()).getIsDeleted()) {
            throw new EventDeleteException("해당 이벤트는 삭제된 이벤트입니다.");
        }
        return eventInfo;
    }

    private void checkDuplicateReview(User user, EventInfo eventInfo) {
        if (reviewRepository.findByUserAndEventInfo(user, eventInfo) != null) {
            throw new DuplicateReviewException("해당 이벤트에 대한 리뷰가 이미 존재합니다.");
        }
    }

    private Review getValidReview(User user, Long reviewId) {
        Review review = reviewRepository.findByUserAndReviewId(user, reviewId);
        if (review == null) {
            throw new NoReviewException("해당 이벤트에 대한 리뷰가 존재하지 않습니다.");
        }
        return review;
    }

    @Transactional
    @KafkaListener(topics = "userTopic")
    public void handleUserSynchronization(KafkaStatus<KafkaUserRequest> status) {
        switch (status.status()) {
            case "signUp":
                userRepository.save(status.data().toEntity(status.data()));
                break;
            case "delete":
                User user = userRepository.findByUserId(status.data().userId());
                user.deleteUser();
                userRepository.save(user);
                break;
            default:
                System.out.println("Unknown status: " + status.status());
        }
    }

    @Transactional
    @KafkaListener(topics = "eventInfoTopic")
    public void handleEventSynchronization(KafkaStatus<Long> status) {
        switch (status.status()) {
            case "create":
                KafkaEventInfoRequest kafkaEventInfoRequest = new KafkaEventInfoRequest(status.data());
                eventInfoRepository.save(kafkaEventInfoRequest.toEntity());
                break;
            case "delete":
                EventInfo eventInfo = eventInfoRepository.findByEventInfoId(status.data());
                eventInfo.deleteEvent();
                break;
            default:
                System.out.println("Unknown status: " + status.status());
        }
    }




}

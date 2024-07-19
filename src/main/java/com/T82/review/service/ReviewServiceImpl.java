package com.T82.review.service;

import com.T82.review.domain.dto.request.AddReviewRequest;
import com.T82.review.domain.dto.response.ReviewResponse;
import com.T82.review.domain.entity.EventInfo;
import com.T82.review.domain.entity.Review;
import com.T82.review.domain.entity.User;
import com.T82.review.domain.repository.ReviewRepository;
import com.T82.review.exception.DuplicateReviewException;
import com.T82.review.exception.NoReviewException;
import com.T82.review.global.utils.TokenInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService{
    private final ReviewRepository reviewRepository;


    @Override
    public void addReview(TokenInfo tokenInfo, AddReviewRequest addReviewRequest) {
        User user = User.builder().userId(tokenInfo.id()).build();
        EventInfo eventInfo = EventInfo.builder().eventInfoId(addReviewRequest.eventInfoId()).build();
        if(reviewRepository.findByUserAndEventInfo(user,eventInfo) != null){
            throw new DuplicateReviewException("해당 이벤트에 대한 리뷰가 이미 존재합니다.");
        }
        reviewRepository.save(addReviewRequest.toEntity(user,eventInfo));
    }

    @Override
    public ReviewResponse getReview(TokenInfo tokenInfo, Long eventInfoId) {
        User user = User.builder().userId(tokenInfo.id()).build();
        EventInfo eventInfo = EventInfo.builder().eventInfoId(eventInfoId).build();
        Review review = reviewRepository.findByUserAndEventInfo(user, eventInfo);
        if (review == null) {
            throw new NoReviewException("해당 이벤트에 대한 리뷰가 존재하지 않습니다.");
        }
        return ReviewResponse.from(review);
    }

}

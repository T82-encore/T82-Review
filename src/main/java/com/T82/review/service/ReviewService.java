package com.T82.review.service;

import com.T82.review.domain.dto.request.AddReviewRequest;
import com.T82.review.domain.dto.response.ReviewResponse;
import com.T82.review.global.utils.TokenInfo;

import java.util.List;

public interface ReviewService {
    void addReview(TokenInfo tokenInfo, AddReviewRequest addReviewRequest);
    List<ReviewResponse> getAllUserReview(TokenInfo tokenInfo);
    List<ReviewResponse> getAllReview(Long eventInfoId);
    void deleteReview(TokenInfo tokenInfo, Long reviewId);
}

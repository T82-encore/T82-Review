package com.T82.review.service;

import com.T82.review.domain.dto.request.AddReviewRequest;
import com.T82.review.domain.dto.response.ReviewResponse;
import com.T82.review.domain.entity.Review;
import com.T82.review.global.utils.TokenInfo;

public interface ReviewService {
    void addReview(TokenInfo tokenInfo, AddReviewRequest addReviewRequest);
    ReviewResponse getReview(TokenInfo tokenInfo, Long eventInfoId);
    void deleteReview(TokenInfo tokenInfo, Long eventInfoId);
}

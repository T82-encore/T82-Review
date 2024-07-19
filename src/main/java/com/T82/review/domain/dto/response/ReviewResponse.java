package com.T82.review.domain.dto.response;

import com.T82.review.domain.entity.Review;

import java.time.LocalDate;

public record ReviewResponse(
        Long eventInfoId,
        String content,
        Double rating,
        String reviewPictureUrl,
        LocalDate createdDate
) {
    public static ReviewResponse from(Review review){
        return new ReviewResponse(review.getEventInfo().getEventInfoId(),review.getContent(),
                review.getRating(),review.getReviewPictureUrl(),review.getCreatedDate());
    }
}

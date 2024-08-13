package com.T82.review.domain.dto.response;

import com.T82.review.domain.entity.Review;
import lombok.Builder;

import java.time.LocalDate;
import java.util.UUID;

@Builder
public record ReviewResponse(
        Long eventInfoId,
        String content,
        Double rating,
        String reviewPictureUrl,
        LocalDate createdDate,
        UUID userId,
        String userName,
        boolean isArtist
) {
    public static ReviewResponse from(Review review) {
        return ReviewResponse.builder()
                .eventInfoId(review.getEventInfo().getEventInfoId())
                .content(review.getContent())
                .rating(review.getRating())
                .reviewPictureUrl(review.getReviewPictureUrl())
                .createdDate(review.getCreatedDate())
                .userId(review.getUser().getUserId())
                .isArtist(review.getUser().getIsArtist())
                .userName(review.getUser().getUsername())
                .isArtist(review.getUser().getIsArtist())
                .build();
    }
}

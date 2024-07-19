package com.T82.review.domain.dto.request;

import com.T82.review.domain.entity.EventInfo;
import com.T82.review.domain.entity.Review;
import com.T82.review.domain.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.UUID;

public record AddReviewRequest (
        @NotNull(message = "공백일 수 없습니다.")
        Long eventInfoId,
        String content,
        @NotNull(message = "공백일 수 없습니다.")
        Double rating,
        String reviewPictureUrl
){
        public Review toEntity(User user, EventInfo eventInfo){
                return Review.builder()
                        .user(user)
                        .eventInfo(eventInfo)
                        .content(content)
                        .rating(rating)
                        .reviewPictureUrl(reviewPictureUrl)
                        .isDeleted(false)
                        .createdDate(LocalDate.now())
                        .build();
        }
}

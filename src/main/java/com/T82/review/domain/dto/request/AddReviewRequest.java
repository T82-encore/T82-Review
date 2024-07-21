package com.T82.review.domain.dto.request;

import com.T82.review.domain.entity.EventInfo;
import com.T82.review.domain.entity.Review;
import com.T82.review.domain.entity.User;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.UUID;

public record AddReviewRequest (
        @NotNull(message = "공백일 수 없습니다.")
        Long eventInfoId,
        String content,
        @NotNull(message = "공백일 수 없습니다.")
        @Min(value = 1, message = "최소 평점은 1입니다.")
        @Max(value = 5, message = "최대 평점은 5입니다.")
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

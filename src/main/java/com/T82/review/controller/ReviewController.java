package com.T82.review.controller;

import com.T82.review.domain.dto.request.AddReviewRequest;
import com.T82.review.global.utils.TokenInfo;
import com.T82.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reviews")
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<String> addReview(@AuthenticationPrincipal TokenInfo tokenInfo,
                                            @Validated @RequestBody AddReviewRequest addReviewRequest) {
        reviewService.addReview(tokenInfo,addReviewRequest);
        return ResponseEntity.ok("리뷰 등록 성공");
    }

}

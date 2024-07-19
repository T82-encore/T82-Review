package com.T82.review.controller;

import com.T82.review.domain.dto.request.AddReviewRequest;
import com.T82.review.domain.dto.response.ReviewResponse;
import com.T82.review.global.utils.TokenInfo;
import com.T82.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping
    public List<ReviewResponse> getAllReview(@AuthenticationPrincipal TokenInfo tokenInfo) {
        return reviewService.getAllReviews(tokenInfo);
    }

    @GetMapping("/{eventInfoId}")
    public ReviewResponse getReview(@AuthenticationPrincipal TokenInfo tokenInfo,
                                    @PathVariable(name = "eventInfoId") Long eventInfoId) {
        return reviewService.getReview(tokenInfo,eventInfoId);
    }

    @DeleteMapping("/{eventInfoId}")
    public ResponseEntity<String> deleteReview(@AuthenticationPrincipal TokenInfo tokenInfo,
                                               @PathVariable(name = "eventInfoId") Long eventInfoId) {
        reviewService.deleteReview(tokenInfo,eventInfoId);
        return ResponseEntity.ok("리뷰 삭제 성공");
    }

}

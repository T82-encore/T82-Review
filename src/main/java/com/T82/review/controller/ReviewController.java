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

    //유저가 쓴 전체 리뷰
    @GetMapping
    public List<ReviewResponse> getAllUserReview(@AuthenticationPrincipal TokenInfo tokenInfo) {
        return reviewService.getAllUserReview(tokenInfo);
    }

    //이벤트에 쓰여있는 전체 리뷰
    @GetMapping("/{eventInfoId}")
    public List<ReviewResponse> getAllReview(@PathVariable(name = "eventInfoId") Long eventInfoId) {
        return reviewService.getAllReview(eventInfoId);
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<String> deleteReview(@AuthenticationPrincipal TokenInfo tokenInfo,
                                               @PathVariable(name = "reviewId") Long reviewId) {
        reviewService.deleteReview(tokenInfo,reviewId);
        return ResponseEntity.ok("리뷰 삭제 성공");
    }

}

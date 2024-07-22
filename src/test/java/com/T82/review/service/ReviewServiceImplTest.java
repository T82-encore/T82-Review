package com.T82.review.service;

import com.T82.review.domain.dto.request.AddReviewRequest;
import com.T82.review.domain.dto.response.ReviewResponse;
import com.T82.review.domain.entity.EventInfo;
import com.T82.review.domain.entity.Review;
import com.T82.review.domain.entity.User;
import com.T82.review.domain.repository.EventInfoRepository;
import com.T82.review.domain.repository.ReviewRepository;
import com.T82.review.domain.repository.UserRepository;
import com.T82.review.exception.DuplicateReviewException;
import com.T82.review.exception.NoReviewException;
import com.T82.review.global.utils.TokenInfo;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ReviewServiceImplTest {
    @Autowired
    ReviewRepository reviewRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    EventInfoRepository eventInfoRepository;
    @Autowired
    ReviewService reviewService;

    private User user;
    private EventInfo eventInfo1;
    private EventInfo eventInfo2;
    @BeforeEach
    void 초기_설정() {
        reviewRepository.deleteAll();
        userRepository.deleteAll();
        eventInfoRepository.deleteAll();

        UUID userId = UUID.randomUUID(); // UUID 생성
        Long eventInfoId1 = 1L; // Long ID 설정
        Long eventInfoId2 = 2L;

        user = userRepository.save(User.builder()
                .userId(userId)
                .email("user@example.com")
                .isDeleted(false)
                .build());

        eventInfo1 = eventInfoRepository.save(EventInfo.builder()
                .eventInfoId(eventInfoId1)
                .isDeleted(false)
                .build());

        eventInfo2 = eventInfoRepository.save(EventInfo.builder()
                .eventInfoId(eventInfoId2)
                .isDeleted(false)
                .build());
    }

    @Nested
    class 리뷰_생성 {
        @Test
        void 성공() {
            // given
            TokenInfo tokenInfo = new TokenInfo(user.getUserId(), user.getEmail());
            AddReviewRequest request = new AddReviewRequest(
                    eventInfo1.getEventInfoId(), "좋아요", 4.5, "http://example.com/image.jpg");

            // when
            reviewService.addReview(tokenInfo, request);

            // then
            List<Review> reviews = reviewRepository.findAll();
            assertEquals(1, reviews.size());
            assertNotNull(reviewRepository.findByUserAndEventInfo(user, eventInfo1));
        }

        @Test
        void 실패_중복_리뷰_생성() {
            // given
            TokenInfo tokenInfo = new TokenInfo(user.getUserId(), user.getEmail());
            AddReviewRequest request = new AddReviewRequest(
                    eventInfo1.getEventInfoId(), "좋아요", 4.5, "http://example.com/image.jpg");

            reviewService.addReview(tokenInfo, request);

            // when & then
            DuplicateReviewException exception = assertThrows(DuplicateReviewException.class, () -> {
                reviewService.addReview(tokenInfo, request);
            });
            assertEquals("해당 이벤트에 대한 리뷰가 이미 존재합니다.", exception.getMessage());
        }
    }

    @Nested
    class 전체_리뷰_조회 {
        @Test
        void 성공() {
            // given
            reviewRepository.save(Review.builder()
                    .user(user)
                    .eventInfo(eventInfo1)
                    .content("좋아요")
                    .rating(4.0)
                    .reviewPictureUrl("http://example.com/image.jpg")
                    .isDeleted(false)
                    .createdDate(LocalDate.now())
                    .build());

            reviewRepository.save(Review.builder()
                    .user(user)
                    .eventInfo(eventInfo2)
                    .content("별로에요")
                    .rating(2.0)
                    .reviewPictureUrl("http://example.com/image2.jpg")
                    .isDeleted(false)
                    .createdDate(LocalDate.now())
                    .build());

            TokenInfo tokenInfo = new TokenInfo(user.getUserId(), user.getEmail());
            // when
            List<ReviewResponse> reviews = reviewService.getAllReviews(tokenInfo);

            // then
            assertNotNull(reviews.get(0).eventInfoId());
            assertNotNull(reviews.get(1).eventInfoId());

            assertEquals(eventInfo1.getEventInfoId(), reviews.get(0).eventInfoId());
            assertEquals(eventInfo2.getEventInfoId(), reviews.get(1).eventInfoId());


        }

        @Test
        void 실패_리뷰_없음() {
            // given
            TokenInfo tokenInfo = new TokenInfo(user.getUserId(), user.getEmail());

            // when & then
            NoReviewException exception = assertThrows(NoReviewException.class, () -> {
                reviewService.getAllReviews(tokenInfo);
            });

            assertEquals("해당 유저가 작성한 리뷰가 존재하지 않습니다.", exception.getMessage());
        }
    }
    @Nested
    class 한_이벤트에_대한_리뷰_조회 {
        @Test
        void 성공() {
            // given
            reviewRepository.save(Review.builder()
                    .user(user)
                    .eventInfo(eventInfo1)
                    .content("좋아요")
                    .rating(4.0)
                    .reviewPictureUrl("http://example.com/image.jpg")
                    .isDeleted(false)
                    .createdDate(LocalDate.now())
                    .build());

            reviewRepository.save(Review.builder()
                    .user(user)
                    .eventInfo(eventInfo2)
                    .content("별로에요")
                    .rating(2.0)
                    .reviewPictureUrl("http://example.com/image2.jpg")
                    .isDeleted(false)
                    .createdDate(LocalDate.now())
                    .build());

            TokenInfo tokenInfo = new TokenInfo(user.getUserId(), user.getEmail());

            // when
            ReviewResponse reviews = reviewService.getReview(tokenInfo, eventInfo1.getEventInfoId());

            // then
            assertEquals(eventInfo1.getEventInfoId(), reviews.eventInfoId());
            assertEquals("좋아요", reviews.content());
            assertEquals(4.0, reviews.rating());
            assertEquals("http://example.com/image.jpg", reviews.reviewPictureUrl());
        }

        @Test
        void 실패_리뷰_없음() {
            // given
            TokenInfo tokenInfo = new TokenInfo(user.getUserId(), user.getEmail());

            // when & then
            NoReviewException exception = assertThrows(NoReviewException.class, () -> {
                reviewService.getReview(tokenInfo, eventInfo1.getEventInfoId());
            });

            assertEquals("해당 이벤트에 대한 리뷰가 존재하지 않습니다.", exception.getMessage());
        }
    }

    @Nested
    class 리뷰_삭제 {
        @Test
        void 성공() {
            // given
            Review review = reviewRepository.save(Review.builder()
                    .user(user)
                    .eventInfo(eventInfo1)
                    .content("좋아요")
                    .rating(4.0)
                    .reviewPictureUrl("http://example.com/image.jpg")
                    .isDeleted(false)
                    .createdDate(LocalDate.now())
                    .build());
            TokenInfo tokenInfo = new TokenInfo(user.getUserId(), user.getEmail());

            // when
            reviewService.deleteReview(tokenInfo,eventInfo1.getEventInfoId());

            // then
            assertEquals(true, review.getIsDeleted());
        }

        @Test
        void 실패_리뷰_없음() {
            // given
            TokenInfo tokenInfo = new TokenInfo(user.getUserId(), user.getEmail());

            // when & then
            NoReviewException exception = assertThrows(NoReviewException.class, () -> {
                reviewService.deleteReview(tokenInfo, eventInfo1.getEventInfoId());
            });

            assertEquals("해당 이벤트에 대한 리뷰가 존재하지 않습니다.", exception.getMessage());
        }
    }
}
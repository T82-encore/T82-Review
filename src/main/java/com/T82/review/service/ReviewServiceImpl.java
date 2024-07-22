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
import com.T82.review.exception.EventDeleteException;
import com.T82.review.exception.NoReviewException;
import com.T82.review.exception.UserDeleteException;
import com.T82.review.global.utils.TokenInfo;
import com.T82.review.kafka.dto.KafkaStatus;
import com.T82.review.kafka.dto.request.KafkaUserDeleteRequest;
import com.T82.review.kafka.dto.request.KafkaUserSignUpRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService{
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final EventInfoRepository eventInfoRepository;


    @Override
    public void addReview(TokenInfo tokenInfo, AddReviewRequest addReviewRequest) {
        User user = getUser(tokenInfo);
        EventInfo eventInfo = getValidEventInfo(addReviewRequest.eventInfoId());
        checkDuplicateReview(user, eventInfo);
        reviewRepository.save(addReviewRequest.toEntity(user, eventInfo));
    }

    @Override
    public List<ReviewResponse> getAllReviews(TokenInfo tokenInfo) {
        User user = getUser(tokenInfo);
        List<Review> allByUser = reviewRepository.findAllByUserAndIsDeleted(user, false);
        if (allByUser.isEmpty()) {
            throw new NoReviewException("해당 유저가 작성한 리뷰가 존재하지 않습니다.");
        }
        return allByUser.stream().map(ReviewResponse::from).toList();
    }

    @Override
    public ReviewResponse getReview(TokenInfo tokenInfo, Long eventInfoId) {
        User user = getUser(tokenInfo);
        EventInfo eventInfo = getValidEventInfo(eventInfoId);
        Review review = getValidReview(user, eventInfo);
        return ReviewResponse.from(review);
    }

    @Override
    public void deleteReview(TokenInfo tokenInfo, Long eventInfoId) {
        User user = getUser(tokenInfo);
        EventInfo eventInfo = getValidEventInfo(eventInfoId);
        Review review = getValidReview(user, eventInfo);
        review.deleteReview();
        reviewRepository.save(review);
    }




    private User getUser(TokenInfo tokenInfo) {
        User user = User.builder().userId(tokenInfo.id()).build();
        if(userRepository.findByUserId(user.getUserId()).getIsDeleted()){
            throw new UserDeleteException("해당 회원은 탈퇴한 회원입니다.");
        }
        return user;
    }

    private EventInfo getValidEventInfo(Long eventInfoId) {
        EventInfo eventInfo = EventInfo.builder().eventInfoId(eventInfoId).build();
        if (eventInfoRepository.findByEventInfoId(eventInfo.getEventInfoId()).getIsDeleted()) {
            throw new EventDeleteException("해당 이벤트는 삭제된 이벤트입니다.");
        }
        return eventInfo;
    }

    private void checkDuplicateReview(User user, EventInfo eventInfo) {
        if (reviewRepository.findByUserAndEventInfo(user, eventInfo) != null) {
            throw new DuplicateReviewException("해당 이벤트에 대한 리뷰가 이미 존재합니다.");
        }
    }

    private Review getValidReview(User user, EventInfo eventInfo) {
        Review review = reviewRepository.findByUserAndEventInfo(user, eventInfo);
        if (review == null) {
            throw new NoReviewException("해당 이벤트에 대한 리뷰가 존재하지 않습니다.");
        }
        return review;
    }


    @Transactional
    @KafkaListener(topics = "signup-topic")
    public void synchronizationSignUp(KafkaStatus<KafkaUserSignUpRequest> status){
        System.out.println(status.data());
        KafkaUserSignUpRequest data = status.data();
        System.out.println(data.userId());
        System.out.println(data.toEntity(data).getUserId());
        User save = userRepository.save(data.toEntity(data));
        System.out.println("최종: "+save.getUserId());
    }

    @Transactional
    @KafkaListener(topics = "delete-topic")
    public void synchronizationDelete(KafkaStatus<KafkaUserDeleteRequest> status){
        User user = userRepository.findByUserId(status.data().userId());
        user.deleteUser();
        userRepository.save(user);
    }
}

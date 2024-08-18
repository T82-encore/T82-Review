package com.T82.review.service.Impl;

import com.T82.common_exception.exception.review.ReviewNotFoundException;
import com.T82.common_exception.exception.user.UserNotFoundException;
import com.T82.review.domain.dto.request.CommentCreateRequest;
import com.T82.review.domain.dto.response.CommentResponse;
import com.T82.review.domain.entity.Comment;
import com.T82.review.domain.entity.Review;
import com.T82.review.domain.entity.User;
import com.T82.review.domain.repository.CommentRepository;
import com.T82.review.domain.repository.ReviewRepository;
import com.T82.review.domain.repository.UserRepository;
import com.T82.review.global.utils.TokenInfo;
import com.T82.review.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;

//    @CustomException(ErrorCode.FAILED_CREATE_COMMENT) "댓글 생성에 실패했습니다."
    @Override
    public void createComment(Long reviewId, TokenInfo tokenInfo, CommentCreateRequest commentCreateRequest) {
       User user = getUser(tokenInfo);

       Review review = reviewRepository.findById(reviewId).orElseThrow(ReviewNotFoundException::new);

        Comment comment = Comment.toEntity(user, review,commentCreateRequest);

        commentRepository.save(comment);
    }

//    @CustomException(ErrorCode.FAILED_GET_COMMENT) "댓글 불러오기에 실패했습니다."
    @Override
    public List<CommentResponse> getCommentList(Long reviewId) {

        Review review = reviewRepository.findById(reviewId).orElseThrow(ReviewNotFoundException::new);

        List<Comment> comments = commentRepository.findAllByReviewReviewId(reviewId);

        return comments.stream().map(CommentResponse::from).toList();
    }

//    @CustomException(ErrorCode.FAILED_DELETE_COMMENT) '댓글 삭제에 실패했습니다."
    @Override
    public void deleteComment(Long reviewId, Long commentId, TokenInfo tokenInfo) {
        User user = getUser(tokenInfo);

        Review review = reviewRepository.findById(reviewId).orElseThrow(ReviewNotFoundException::new);

        Comment comment = commentRepository.findById(commentId).filter(c -> c.getUser().getUserId().equals(user.getUserId()))
                .orElseThrow(() -> new IllegalArgumentException("댓글 삭제 권한 없음"));

        commentRepository.deleteById(commentId);
    }


    private User getUser(TokenInfo tokenInfo) {
        User user = User.builder().userId(tokenInfo.id()).build();
        if(userRepository.findByUserId(user.getUserId()).getIsDeleted()){
            throw new UserNotFoundException();
        }
        return user;
    }
}

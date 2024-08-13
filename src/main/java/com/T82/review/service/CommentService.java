package com.T82.review.service;

import com.T82.review.domain.dto.request.CommentCreateRequest;
import com.T82.review.domain.dto.response.CommentResponse;
import com.T82.review.global.utils.TokenInfo;

import java.util.List;

public interface CommentService {
    void createComment(Long reviewId, TokenInfo tokenInfo, CommentCreateRequest commentCreateRequest);

    List<CommentResponse> getCommentList(Long reviewId);

    void deleteComment(Long reviewId, Long commentId, TokenInfo tokenInfo);
}

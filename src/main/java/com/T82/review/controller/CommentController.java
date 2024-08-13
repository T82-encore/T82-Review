package com.T82.review.controller;

import com.T82.review.domain.dto.request.CommentCreateRequest;
import com.T82.review.domain.dto.response.CommentResponse;
import com.T82.review.global.utils.TokenInfo;
import com.T82.review.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/{reviewId}/comments")
public class CommentController {
    private final CommentService commentService;

    @PostMapping()
    public void createComment (@PathVariable("reviewId") Long reviewId
            ,@AuthenticationPrincipal TokenInfo tokenInfo
            ,@RequestBody CommentCreateRequest commentCreateRequest){

        commentService.createComment(reviewId,tokenInfo ,commentCreateRequest);
    }

    @GetMapping()
    public List<CommentResponse> getCommentList (@PathVariable("reviewId") Long reviewId){
        return  commentService.getCommentList(reviewId);
    }

    @DeleteMapping("/{commentId}")
    public void deleteComment(@PathVariable("reviewId") Long reviewId,
                              @PathVariable("commentId") Long commentId,
                              @AuthenticationPrincipal TokenInfo tokenInfo
                              ){
        commentService.deleteComment(reviewId, commentId ,tokenInfo);
    }



}

package com.T82.review.domain.dto.response;

import com.T82.review.domain.entity.Comment;
import com.T82.review.domain.entity.User;
import lombok.Builder;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Builder
public record CommentResponse(Long commentId,
                              String content,
                              boolean isDeleted,
                              LocalDateTime createdDate,
                              UUID userId,
                              String userImage,
                              String username,
                              boolean isArtist
){

    public static CommentResponse from(Comment comment){
        return CommentResponse.builder()
                .content(comment.getContent())
                .isDeleted(comment.getIsDeleted())
                .createdDate(comment.getCreatedDate())
                .userId(comment.getUser().getUserId())
                .userImage(comment.getUser().getImageUrl())
                .username(comment.getUser().getUsername())
                .isArtist(comment.getUser().getIsArtist())
                .build();
    }
}

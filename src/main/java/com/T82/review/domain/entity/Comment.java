package com.T82.review.domain.entity;

import com.T82.review.domain.dto.request.CommentCreateRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
@Table(name = "COMMENTS")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COMMENT_ID")
    private Long commentId;

    @Column(name = "CONTENT", nullable = false)
    private String content;

    @Column(name = "IS_DELETED" , nullable = false)
    private Boolean isDeleted;


    @Column(name = "CREATED_DATE", nullable = false)
    @CreatedDate
    private LocalDateTime createdDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "REVIEW_ID", nullable = false)
    private Review review;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;


    @PrePersist
    protected void onCreate() {
        if (isDeleted == null) isDeleted = false;
    }

    public static Comment toEntity(User user,Review review,CommentCreateRequest commentCreateRequest){
        return Comment.builder()
                .user(user)
                .review(review)
                .content(commentCreateRequest.content())
                .build();
    }
}

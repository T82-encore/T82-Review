package com.T82.review.domain.repository;

import com.T82.review.domain.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Long> {


    List<Comment> findAllByReviewReviewId(Long reviewId);
}

package com.T82.review.domain.repository;

import com.T82.review.domain.entity.EventInfo;
import com.T82.review.domain.entity.Review;
import com.T82.review.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    Review findByUserAndEventInfo(User user, EventInfo eventInfo);
    List<Review> findAllByUser(User user);
    List<Review> findAllByUserAndIsDeleted(User user, Boolean isDeleted);
}

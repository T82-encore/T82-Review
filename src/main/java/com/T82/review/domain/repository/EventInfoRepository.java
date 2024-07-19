package com.T82.review.domain.repository;

import com.T82.review.domain.entity.EventInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventInfoRepository extends JpaRepository<EventInfo, Long> {
}

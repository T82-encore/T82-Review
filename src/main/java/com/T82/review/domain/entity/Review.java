package com.T82.review.domain.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "Reviews")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "REVIEW_ID")
    private Long reviewId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false)
//    @JsonBackReference
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EVENT_INFO_ID", nullable = false)
//    @JsonBackReference
    private EventInfo eventInfo;

    @Column(name = "CONTENT")
    private String content;

    @Column(name = "RATING", nullable = false)
    private Double rating;

    @Column(name = "REVIEW_PICTURE_URL")
    private String reviewPictureUrl;

    @Column(name = "IS_DELETED", nullable = false)
    private Boolean isDeleted;

    @Column(name = "CREATED_DATE", nullable = false)
    private LocalDate createdDate;


}

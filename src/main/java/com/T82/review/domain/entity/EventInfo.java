package com.T82.review.domain.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "Reviews_Event_Infos")
public class EventInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "EVENT_INFO_ID", nullable = false)
    private Long eventInfoId;

    @Column(name = "IS_DELETED", nullable = false)
    private Boolean isDeleted;

    @OneToMany(mappedBy = "eventInfo", cascade = CascadeType.ALL, orphanRemoval = true)
//    @JsonManagedReference
    private List<Review> review;
}

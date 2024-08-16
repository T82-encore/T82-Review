package com.T82.review.domain.entity;

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
    @Column(name = "EVENT_INFO_ID", nullable = false)
    private Long eventInfoId;

    @Column(name = "IS_DELETED", nullable = false)
    private Boolean isDeleted;

    @OneToMany(mappedBy = "eventInfo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> review;

    public void deleteEvent() {
        this.isDeleted = true;
    }
}

package com.T82.review.domain.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "Reviews_Users")
public class User {
    @Id
//    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "USER_ID", nullable = false)
    private UUID userId;

    @Column(name = "EMAIL", nullable = false)
    private String email;

    @Column(name = "USER_NAME", nullable = false)
    private String username;

    @Column(name = "IS_DELETED", nullable = false)
    private Boolean isDeleted;

    @Column(name = "IMAGE_URL")
    private String ImageUrl;

    @Column(name = "IS_ARTIST", nullable = false)
    private Boolean isArtist;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
//    @JsonManagedReference
    private List<Review> reviews;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
//    @JsonManagedReference
    private List<Comment> comments;

    public void deleteUser() {
        this.isDeleted = true;
    }
}

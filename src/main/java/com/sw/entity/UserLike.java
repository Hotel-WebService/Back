package com.sw.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "user_likes")
public class UserLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "likeID")
    private Long likeID;

    @Column(name = "userID", nullable = false)
    private Long userID;

    @Column(name = "hotelID", nullable = false)
    private Long hotelID;

    @Column(name = "likedat")
    private LocalDateTime likedat;

    // 기본 생성자
    public UserLike() {}

    public UserLike(Long userID, Long hotelID) {
        this.userID = userID;
        this.hotelID = hotelID;
        this.likedat = LocalDateTime.now();
    }

    // Getter/Setter
}
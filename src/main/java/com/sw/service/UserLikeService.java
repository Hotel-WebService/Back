package com.sw.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sw.entity.UserLike;
import com.sw.repository.UserLikeRepository;



@Service
@Transactional
public class UserLikeService {

    @Autowired
    private UserLikeRepository repo;

    // 찜하기 (중복방지)
    public void likeHotel(Long userID, Long hotelID) {
        repo.findByUserIDAndHotelID(userID, hotelID)
            .orElseGet(() -> repo.save(new UserLike(userID, hotelID)));
    }

    // 찜 해제
    public void unlikeHotel(Long userID, Long hotelID) {
        repo.deleteByUserIDAndHotelID(userID, hotelID);
    }

    // 내 찜 목록 조회
    public List<UserLike> getLikedHotels(Long userID) {
        return repo.findByUserID(userID);
    }
}
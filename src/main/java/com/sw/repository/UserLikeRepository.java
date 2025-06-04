package com.sw.repository;

import com.sw.entity.UserLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserLikeRepository extends JpaRepository<UserLike, Long> {
	List<UserLike> findByUserID(Long userID);

	Optional<UserLike> findByUserIDAndHotelID(Long userID, Long hotelID);

	void deleteByUserIDAndHotelID(Long userID, Long hotelID);
}
package com.sw.controller;

import com.sw.entity.UserLike;
import com.sw.service.UserLikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/likes")
public class UserLikeController {

	@Autowired
	UserLikeService service;

	// 찜하기
	@PostMapping
	public void like(@RequestBody Map<String, Long> req) {
		service.likeHotel(req.get("userID"), req.get("hotelID"));
	}

	// 찜 해제
	@DeleteMapping
	public void unlike(@RequestParam Long userID, @RequestParam Long hotelID) {
		service.unlikeHotel(userID, hotelID);
	}

	// 내 찜 목록 조회
	@GetMapping
	public List<UserLike> getLikes(@RequestParam Long userID) {
		return service.getLikedHotels(userID);
	}
}
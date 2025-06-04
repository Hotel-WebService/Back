package com.sw.controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.sw.entity.HotelUser;
import com.sw.entity.Reservation;
import com.sw.entity.ReviewTable;
import com.sw.entity.Room;
import com.sw.repository.HotelUserRepository;
import com.sw.repository.ReservationRepository;
import com.sw.repository.ReviewRepository;
import com.sw.repository.RoomRepository;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

	@Autowired
	private ReviewRepository reviewRepo;

	@Autowired
	private ReservationRepository reservationRepo;

	@Autowired
	private RoomRepository roomRepo;

	@Autowired
	private HotelUserRepository userRepo; // ⭐ 추가

	// 리뷰 등록 (예약자 본인 + 호텔ID 검증)

	@PostMapping
	public ReviewTable createReview(@RequestBody ReviewTable review, Principal principal) {
		System.out.println("principal: " + principal);
		if (principal == null)
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인 필요");

		// 1. principal.getName()으로 loginID(String) 얻기
		String loginID = principal.getName();
		System.out.println("principal.getName = " + loginID);
		// 2. loginID로 user 찾아서 userID 얻기
		HotelUser user = userRepo.findByLoginID(loginID)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "유저 없음"));
		Long loginUserID = user.getUserID();

		// 3. 본인 검증
		if (!Objects.equals(loginUserID, review.getUserID())) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "내 계정이 아님");
		}

		// 4. 예약ID로 예약 정보 가져오기
		Reservation reservation = reservationRepo.findById(review.getReservationID())
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "예약이 존재하지 않음"));

		// 5. 예약이 본인 것인지 검증
		if (!Objects.equals(reservation.getUserID(), loginUserID)) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "내 예약이 아님");
		}

		// 6. roomID로 hotelID 찾아서 호텔 검증
		Room room = roomRepo.findById(reservation.getRoomID())
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "객실이 존재하지 않음"));

		if (!Objects.equals(room.getHotelID(), review.getHotelID())) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "잘못된 호텔ID");
		}

		// 7. 리뷰 날짜 입력 및 저장
		review.setCommentDate(LocalDateTime.now());
		return reviewRepo.save(review);
	}

	// 호텔별 리뷰 리스트
	@GetMapping("/hotel/{hotelID}")
	public Iterable<ReviewTable> getHotelReviews(@PathVariable Long hotelID) {
		return reviewRepo.findByHotelID(hotelID);
	}

	// 리뷰 LISTPAGE 연동
	@GetMapping
	public List<ReviewTable> getAllReviews() {
		return reviewRepo.findAll();
	}

}
package com.sw.controller;

import java.time.LocalDate;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sw.entity.RoomQuantity;
import com.sw.service.RoomQuantityService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/room-quantity")
@RequiredArgsConstructor
public class RoomQuantityController {

	private final RoomQuantityService roomQuantityService;

	/*
	 * [GET] 단순 재고 조회 (없으면 0으로 생성) - 예약 버튼 누르면, 먼저 이걸로 방이 있는지/수량 확인!
	 */
	@GetMapping
	public RoomQuantity getRoomQuantity(@RequestParam Long roomID, @RequestParam String date) {
		LocalDate d = LocalDate.parse(date);
		return roomQuantityService.getOrCreateQuantity(roomID, d);
	}

	/*
	 * [POST] 예약 확정 시 재고 차감 - 결제 성공 후에만 호출!
	 */
	@PostMapping("/reserve")
	public boolean reserve(@RequestParam Long roomID, @RequestParam String date,
			@RequestParam(defaultValue = "1") int count) {
		return roomQuantityService.reserveRoom(roomID, LocalDate.parse(date), count);
	}

	// 예약 취소 시 룸 사용 용량 증가
	@PostMapping("/cancel")
	public boolean cancelReserve(@RequestParam Long roomID, @RequestParam String date,
			@RequestParam(defaultValue = "1") int count) {
		return roomQuantityService.cancelRoomReserve(roomID, LocalDate.parse(date), count);
	}

}

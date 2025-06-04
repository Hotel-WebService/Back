package com.sw.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sw.entity.Room;
import com.sw.service.RoomService;

@RestController
@RequestMapping("/api/rooms")
@CrossOrigin(origins = "*") // React와 CORS 문제 방지 (도메인 맞게 조정)
public class RoomController {

	private final RoomService roomService;

	public RoomController(RoomService roomService) {
		this.roomService = roomService;
	}

	@GetMapping
	public List<Room> getAllRooms() {
		return roomService.getAllRooms();
	}

	// 호텔별 조회 필요하면 아래 주석 해제
	@GetMapping("/hotel/{hotelId}")
	public List<Room> getRoomsByHotel(@PathVariable Long hotelId) {
		return roomService.getRoomsByHotel(hotelId);
	}

}
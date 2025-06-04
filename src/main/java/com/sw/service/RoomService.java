package com.sw.service;

import com.sw.entity.Room;
import com.sw.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomService {
	private final RoomRepository roomRepository;

	public RoomService(RoomRepository roomRepository) {
		this.roomRepository = roomRepository;
	}

	public List<Room> getAllRooms() {
		return roomRepository.findAll();
	}

	// 호텔별 조회 필요하면 추가
	public List<Room> getRoomsByHotel(Long hotelId) {
		return roomRepository.findByHotelID(hotelId);
	}
}
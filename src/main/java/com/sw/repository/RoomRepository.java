package com.sw.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sw.entity.Room;

public interface RoomRepository extends JpaRepository<Room, Long> {
    //호텔별 객실 조회 기능
	List<Room> findByHotelID(Long hotelID);
}
package com.sw.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sw.entity.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

	// (JPQL)
	@Query("SELECT r FROM Reservation r JOIN Room room ON r.roomID = room.roomID "
			+ "WHERE r.userID = :userID AND room.hotelID = :hotelID")
	List<Reservation> findByUserIDAndHotelID(@Param("userID") Long userID, @Param("hotelID") Long hotelID);
	// 체크아웃 날짜 & 상태로 예약 리스트 조회 (예: 체크아웃한 예약만)
	@Query("SELECT r FROM Reservation r WHERE r.check_out_date = :date AND r.status = :status")
    List<Reservation> findByCheckOutDateAndStatus(@Param("date") LocalDate date, @Param("status") Reservation.Status status);
} 
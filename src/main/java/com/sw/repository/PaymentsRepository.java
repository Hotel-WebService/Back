package com.sw.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sw.entity.Payments;
import com.sw.dto.PaymentsDTO;

public interface PaymentsRepository extends JpaRepository<Payments, Long> {
	   List<Payments> findByUserID(Long userID);
	   
	   @Query("""
				SELECT new com.sw.dto.PaymentsDTO(
				  p.paymentID,
				  h.hotelName,
				  r.room_name,
				  p.amount,
				  p.payment_method,
				  p.payment_status,
				  p.pay_date
				)
				FROM Payments p
				JOIN Reservation rsv ON p.reservationID = rsv.reservationID
				JOIN Room r ON rsv.roomID = r.roomID
				JOIN Hotel h ON r.hotelID = h.hotelID
				WHERE p.userID = :userId
				""")
				List<PaymentsDTO> findPaymentDetailsByUserId(@Param("userId") Long userId);
	}

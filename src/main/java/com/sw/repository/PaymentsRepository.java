package com.sw.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.sw.dto.PaymentsDTO;
import com.sw.entity.Payments;

public interface PaymentsRepository extends JpaRepository<Payments, Long> {
	List<Payments> findByUserID(Long userID);

	void deleteByReservationID(Long reservationID);

	@Query("""
			SELECT new com.sw.dto.PaymentsDTO(
			  p.paymentID,
			  p.reservationID,
			  h.hotelID,
						  r.roomID,
			  h.hotelName,
			  r.room_name,
			  p.amount,
			  p.payment_method,
			  p.payment_status,
			  p.pay_date,
			  rsv.check_in_date,
			  rsv.check_out_date
			)
			FROM Payments p
			JOIN Reservation rsv ON p.reservationID = rsv.reservationID
			JOIN Room r ON rsv.roomID = r.roomID
			JOIN Hotel h ON r.hotelID = h.hotelID
			WHERE p.userID = :userId
			""")
	List<PaymentsDTO> findPaymentDetailsByUserId(@Param("userId") Long userId);
	
	// 💡 결제상태를 'N'으로 변경하는 update 쿼리 추가
    @Modifying
    @Transactional
    @Query("UPDATE Payments p SET p.payment_status = 'N' WHERE p.reservationID = :reservationId")
    void updateStatusToNByReservationID(@Param("reservationId") Long reservationId);
}
 
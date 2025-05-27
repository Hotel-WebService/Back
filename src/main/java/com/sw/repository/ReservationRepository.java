package com.sw.repository;

import com.sw.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    // (JPQL)
    @Query("SELECT r FROM Reservation r JOIN Room room ON r.roomID = room.roomID " +
           "WHERE r.userID = :userID AND room.hotelID = :hotelID")
    List<Reservation> findByUserIDAndHotelID(@Param("userID") Long userID, @Param("hotelID") Long hotelID);
}
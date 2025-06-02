package com.sw.repository;

import com.sw.entity.RoomQuantity;
import com.sw.entity.RoomQuantityId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoomQuantityRepository extends JpaRepository<RoomQuantity, RoomQuantityId> {
    Optional<RoomQuantity> findByIdRoomIDAndIdDate(Long roomID, java.time.LocalDate date);
}

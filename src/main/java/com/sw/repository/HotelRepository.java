package com.sw.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sw.dto.HotelRoomDTO;
import com.sw.entity.Hotel;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {
    // 기본 메소드
    Optional<Hotel> findByHotelName(String hotelName);
    List<Hotel> findByCityAndDistrict(String city, String district);

    // 연관관계 선언 없이 JOIN hotelID 수동 사용
    @Query("""
        SELECT new com.sw.dto.HotelRoomDTO(
            h.hotelID, h.hotelName, h.star, h.district, h.city, h.parkingLot,
            r.roomID, r.price, r.capacity
        )
        FROM Hotel h
        JOIN Room r ON h.hotelID = r.hotelID
        WHERE h.city = :city
          AND h.district = :district
          AND (:star IS NULL OR h.star = :star)
          AND (:parkingLot IS NULL OR h.parkingLot = :parkingLot)
          AND (:capacity IS NULL OR r.capacity >= :capacity)
          AND (:minPrice IS NULL OR r.price >= :minPrice)
          AND (:maxPrice IS NULL OR r.price <= :maxPrice)
    """)
    List<HotelRoomDTO> findHotelRoomByFilter(
        @Param("city") String city,
        @Param("district") String district,
        @Param("star") String star,
        @Param("parkingLot") Boolean parkingLot,
        @Param("capacity") Integer capacity,
        @Param("minPrice") Integer minPrice,
        @Param("maxPrice") Integer maxPrice
    );
    
 // (2) [선택] Hotel 엔티티로만 반환 (필요시 활용)
    @Query("""
        SELECT DISTINCT h
        FROM Hotel h
        JOIN Room r ON h.hotelID = r.hotelID
        WHERE h.city = :city
          AND h.district = :district
          AND (:star IS NULL OR h.star = :star)
          AND (:parkingLot IS NULL OR h.parkingLot = :parkingLot)
          AND (:capacity IS NULL OR r.capacity >= :capacity)
          AND (:minPrice IS NULL OR r.price >= :minPrice)
          AND (:maxPrice IS NULL OR r.price <= :maxPrice)
    """)
    List<Hotel> filterHotels(
        @Param("city") String city,
        @Param("district") String district,
        @Param("star") String star,
        @Param("parkingLot") Boolean parkingLot,
        @Param("capacity") Integer capacity,
        @Param("minPrice") Integer minPrice,
        @Param("maxPrice") Integer maxPrice
    );
}

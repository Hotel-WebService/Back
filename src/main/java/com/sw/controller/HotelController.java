package com.sw.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sw.entity.Hotel;
import com.sw.repository.HotelRepository;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/hotels")
public class HotelController {

	private final HotelRepository hotelRepository;

	@Autowired
	public HotelController(HotelRepository hotelRepository) {
		this.hotelRepository = hotelRepository;
	}

	/** 전체 호텔 리스트 조회 → GET /api/hotels */
	@GetMapping
	public ResponseEntity<List<Map<String, Object>>> getAllHotels() {
		List<Map<String, Object>> list = hotelRepository.findAll().stream().map(this::toMap)
				.collect(Collectors.toList());
		return ResponseEntity.ok(list);
	}

	/** 단일 호텔 조회 by ID → GET /api/hotels/{id} */
	@GetMapping("/{id}")
	public ResponseEntity<Map<String, Object>> getHotelById(@PathVariable Long id) {
		Optional<Hotel> opt = hotelRepository.findById(id);
		return opt.map(hotel -> ResponseEntity.ok(toMap(hotel))).orElseGet(() -> ResponseEntity.notFound().build());
	}

	/** 단일 호텔 조회 by 이름 → GET /api/hotels/search?name=제주도 */
	@GetMapping("/search")
	public ResponseEntity<Map<String, Object>> getHotelByName(@RequestParam String name) {
		Optional<Hotel> opt = hotelRepository.findByHotelName(name);
		return opt.map(hotel -> ResponseEntity.ok(toMap(hotel))).orElseGet(() -> ResponseEntity.notFound().build());
	}

	/** 엔티티 → JSON용 Map 변환 헬퍼 (위도, 경도 포함) */
	private Map<String, Object> toMap(Hotel h) {
		Map<String, Object> m = new HashMap<>();
		m.put("hotelID", h.getHotelID());
		m.put("hotelName", h.getHotelName());
		m.put("description", h.getDescription());
		m.put("zip_code", h.getZipCode());
		m.put("address", h.getAddress());
		m.put("city", h.getCity());
		m.put("district", h.getDistrict());
		m.put("latitude", h.getLatitude());
		m.put("longitude", h.getLongitude());
		m.put("hotelnumber", h.getHotelnumber());
		m.put("rooms_count", h.getRoomsCount());
		m.put("parking_lot", h.getParkingLot());
		m.put("check_in", h.getCheckIn());
		m.put("check_out", h.getCheckOut());
		m.put("star", h.getStar());
		m.put("facilities", h.getFacilities());
		return m;
	}
	

	@GetMapping("/filter")
	public ResponseEntity<List<Map<String, Object>>> getHotelsByCityDistrict(
	        @RequestParam String city,
	        @RequestParam String district,
	        @RequestParam(required = false) String star,
	        @RequestParam(required = false) Integer parking_lot,
	        @RequestParam(required = false) Integer capacity,
	        @RequestParam(required = false) Integer minPrice,
	        @RequestParam(required = false) Integer maxPrice,
	        @RequestParam(required = false) String check_in
	) {
	    // 콘솔 확인용 로그
	    System.out.println("필터 파라미터 - city: " + city + ", district: " + district
	            + ", star: " + star
	            + ", parking_lot: " + parking_lot
	            + ", capacity: " + capacity
	            + ", minPrice: " + minPrice
	            + ", maxPrice: " + maxPrice
	            + ", check_in: " + check_in);

	    // 1: 주차 가능, 0: 주차 불가, null: 무관
	    Boolean parkingLot = parking_lot == null ? null : parking_lot == 1;

	    List<Hotel> hotels = hotelRepository.filterHotels(
	            city,
	            district,
	            star,
	            parkingLot,
	            capacity,
	            minPrice,
	            maxPrice
	    );
	    List<Map<String, Object>> list = hotels.stream().map(this::toMap).collect(Collectors.toList());
	    return ResponseEntity.ok(list);
	}
	
	
}

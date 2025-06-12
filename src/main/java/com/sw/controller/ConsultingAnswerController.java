package com.sw.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sw.dto.ConsultingAnswerRequest;
import com.sw.dto.HotelRoomDTO;
import com.sw.entity.ConsultingAnswer;
import com.sw.repository.ConsultingAnswerRepository;
import com.sw.repository.HotelRepository;

@RestController
@RequestMapping("/api/consulting")
public class ConsultingAnswerController {
    @Autowired
    private ConsultingAnswerRepository consultingAnswerRepository;
    @Autowired
    private HotelRepository hotelRepository;
/*
    // 1. consulting_answer 저장
    @PostMapping("/save")
    public void saveAnswer(@RequestBody ConsultingAnswerDTO dto) {
        ConsultingAnswer answer = new ConsultingAnswer();
        answer.setUser_id(dto.getUserId());
        answer.setDistrict(dto.getDistrict());
        answer.setStar(dto.getStar());
        answer.setParking_lot(dto.getParking_lot());
        answer.setCapacity(dto.getCapacity());
        answer.setPrice(dto.getPrice());
        answer.setCheck_in(dto.getCheck_in());
        answer.setTheme(dto.getTheme());
        answer.setMood(dto.getMood());
        answer.setSpecial(dto.getSpecial());
        consultingAnswerRepository.save(answer);
        
    }
*/ 
    @PostMapping("/save")
    public String saveConsultingAnswer(@RequestBody ConsultingAnswerRequest req) {
    	 System.out.println(">>> DTO 값: " + req);
        ConsultingAnswer answer = new ConsultingAnswer();
        answer.setUser_id(req.getUserId());
        answer.setDistrict(req.getDistrict());
        answer.setStar(req.getStar());
        answer.setParking_lot(req.getParkingLot());
        answer.setCapacity(req.getCapacity());
        answer.setPrice(req.getPrice());
        answer.setCheck_in(req.getCheckIn());
        answer.setTheme(req.getTheme());
        answer.setMood(req.getMood());
        answer.setSpecial(req.getSpecial());
        consultingAnswerRepository.save(answer);
        return "ok";
    }
    
    
    // 2. 호텔 후보 필터 API
    @GetMapping("/hotel-filter")
    public List<HotelRoomDTO> filterHotels(
        @RequestParam String city,
        @RequestParam String district,
        @RequestParam(required = false) String star,
        @RequestParam(required = false) String parking_lot,
        @RequestParam(required = false) String capacity,
        @RequestParam(required = false) String price
    ) {
        // 파라미터 가공 예시
        Integer minPrice = null, maxPrice = null;
        if (price != null) {
            if (price.contains("~")) {
                String[] arr = price.replaceAll("[^0-9~]", "").split("~");
                minPrice = Integer.parseInt(arr[0] + "0000");
                maxPrice = Integer.parseInt(arr[1] + "0000");
            } else if (price.contains("미만")) {
                maxPrice = Integer.parseInt(price.replaceAll("[^0-9]", "")) * 10000;
            } else if (price.contains("이상")) {
                minPrice = Integer.parseInt(price.replaceAll("[^0-9]", "")) * 10000;
            }
        }
        Integer capacityInt = null;
        if (capacity != null && capacity.matches("\\d+")) {
            capacityInt = Integer.parseInt(capacity.replaceAll("[^0-9]", ""));
        }
        Boolean parkingBool = null;
        if (parking_lot != null) {
            parkingBool = "필수".equals(parking_lot);
        }

        return hotelRepository.findHotelRoomByFilter(
            city, district, star, parkingBool, capacityInt, minPrice, maxPrice
        );
    }
}

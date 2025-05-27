package com.sw.controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sw.entity.HotelUser;
import com.sw.entity.Reservation;
import com.sw.repository.HotelUserRepository;
import com.sw.repository.ReservationRepository;
import com.sw.service.ReservationService;

@RestController
@RequestMapping("/api/reservation")
@CrossOrigin(origins = "*")
public class ReservationController {
	
	@Autowired
	private ReservationRepository reservationRepo;
	
	@Autowired
	HotelUserRepository userRepo;

    private final ReservationService reservationService;
    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public Reservation createReservation(@RequestBody Reservation reservation) {
        reservation.setStatus(Reservation.Status.예약완료);
        reservation.setReservationDate(LocalDateTime.now());
        return reservationService.save(reservation);
    }
    /*
    @GetMapping("/my")
    public List<Reservation> getMyReservations(@RequestParam Long hotelID, Principal principal) {
    	System.out.println("principal.getName(): " + principal.getName());
        Long userID = Long.parseLong(principal.getName()); // principal.getName()이 userID가 맞는지 확인
        return reservationRepo.findByUserIDAndHotelID(userID, hotelID);
    }*/
    
    @GetMapping("/my")
    public List<Reservation> getMyReservations(@RequestParam Long hotelID, Principal principal) {
        // principal.getName()이 username일 경우
        HotelUser user = userRepo.findByLoginID(principal.getName())
            .orElseThrow(() -> new RuntimeException("사용자 없음"));
        Long userID = user.getUserID();
        return reservationRepo.findByUserIDAndHotelID(userID, hotelID);
    }
}

package com.sw.controller;

import com.sw.entity.Reservation;
import com.sw.service.ReservationService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/reservation")
@CrossOrigin(origins = "*")
public class ReservationController {

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
}

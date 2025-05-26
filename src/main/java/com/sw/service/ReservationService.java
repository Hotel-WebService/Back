package com.sw.service;

import com.sw.entity.Reservation;
import com.sw.repository.ReservationRepository;
import org.springframework.stereotype.Service;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }
    public Reservation save(Reservation reservation) {
        return reservationRepository.save(reservation);
    }
}

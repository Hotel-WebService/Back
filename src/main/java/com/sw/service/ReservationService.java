package com.sw.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.sw.entity.Reservation;
import com.sw.repository.ReservationRepository;

@Service
public class ReservationService {
	private final ReservationRepository reservationRepository;

	public ReservationService(ReservationRepository reservationRepository) {
		this.reservationRepository = reservationRepository;
	}

	public Reservation save(Reservation reservation) {
		return reservationRepository.save(reservation);
	}

	public void deleteById(Long reservationID) {
		reservationRepository.deleteById(reservationID);
	}

	public Reservation findById(Long reservationID) {
		Optional<Reservation> opt = reservationRepository.findById(reservationID);
		return opt.orElse(null); // 없으면 null 반환
	}
}

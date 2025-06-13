package com.sw.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "reservationtable")
@Data
public class Reservation {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long reservationID;

	private Long userID;
	private Long roomID;

	private LocalDate check_in_date;
	private LocalDate check_out_date;

	@Enumerated(EnumType.STRING)
	private Status status;

	private LocalDateTime reservationDate;

	public enum Status {
		Y, N
	}
}
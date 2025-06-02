package com.sw.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Getter;

@Getter
public class PaymentsDTO {
	private Long paymentID;
	private Long reservationID;
	private Long hotelID; 
    private Long roomID; 
	private String hotelName;
	private String roomName;
	private BigDecimal amount;
	private String payment_method;
	private String payment_status;
	private LocalDateTime pay_date;
	private LocalDate check_in_date;
    private LocalDate check_out_date;

	public PaymentsDTO(Long paymentID, Long reservationID, Long hotelID, Long roomID, String hotelName, String roomName, BigDecimal amount, String payment_method,
			String payment_status, LocalDateTime pay_date, LocalDate check_in_date, LocalDate check_out_date) {
		this.paymentID = paymentID;
		this.reservationID = reservationID;
		this.hotelID = hotelID;
        this.roomID = roomID;
		this.hotelName = hotelName;
		this.roomName = roomName;
		this.amount = amount;
		this.payment_method = payment_method;
		this.payment_status = payment_status;
		this.pay_date = pay_date;
		this.check_in_date = check_in_date;
		this.check_out_date = check_out_date;
	}
}
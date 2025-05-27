package com.sw.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Getter;

@Getter
public class PaymentsDTO {
	private Long paymentID;
	private String hotelName;
	private String roomName;
	private BigDecimal amount;
	private String payment_method;
	private String payment_status;
	private LocalDateTime pay_date;

	public PaymentsDTO(Long paymentID, String hotelName, String roomName, BigDecimal amount, String payment_method,
			String payment_status, LocalDateTime pay_date) {
		this.paymentID = paymentID;
		this.hotelName = hotelName;
		this.roomName = roomName;
		this.amount = amount;
		this.payment_method = payment_method;
		this.payment_status = payment_status;
		this.pay_date = pay_date;
	}
}
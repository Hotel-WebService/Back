package com.sw.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sw.dto.PaymentsDTO;
import com.sw.entity.Payments;
import com.sw.service.PaymentsService;

@RestController
@RequestMapping("/api/payment")
@CrossOrigin(origins = "*")
public class PaymentsTableController {

	private final PaymentsService paymentsService;

	public PaymentsTableController(PaymentsService paymentsService) {
		this.paymentsService = paymentsService;
	}

	@PostMapping
	public Payments createPayment(@RequestBody Payments payments) {
		payments.setPay_date(LocalDateTime.now());
		return paymentsService.save(payments);
	}

	@GetMapping("/user/{userId}")
	public List<Payments> getPaymentsByUserID(@PathVariable Long userId) {
		return paymentsService.findByUserID(userId);
	}

	@GetMapping("/user/{userId}/details")
	public List<PaymentsDTO> getUserPaymentDetails(@PathVariable Long userId) {
		return paymentsService.getPaymentDetailsByUserId(userId);
	}

	@DeleteMapping("/{paymentId}")
	public void deletePayment(@PathVariable Long paymentId) {
		paymentsService.deleteById(paymentId);
	}
}

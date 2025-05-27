package com.sw.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sw.dto.PaymentsDTO;
import com.sw.entity.Payments;
import com.sw.repository.PaymentsRepository;

@Service
public class PaymentsService {
    private final PaymentsRepository paymentsRepository;
    
    public PaymentsService(PaymentsRepository paymentsRepository) {
        this.paymentsRepository = paymentsRepository;
    }
    
    public Payments save(Payments payments) {
        return paymentsRepository.save(payments);
    }
    
    public List<Payments> findByUserID(Long userID) {
            return paymentsRepository.findByUserID(userID);
        }
    
	public List<PaymentsDTO> getPaymentDetailsByUserId(Long userId) {
		return paymentsRepository.findPaymentDetailsByUserId(userId);
	}
	
	public void deleteById(Long paymentId) {
	    paymentsRepository.deleteById(paymentId);
	}
}

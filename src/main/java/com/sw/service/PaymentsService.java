package com.sw.service;

import java.util.List;

import org.springframework.stereotype.Service;

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
}

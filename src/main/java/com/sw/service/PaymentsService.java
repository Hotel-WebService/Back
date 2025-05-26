package com.sw.service;

import com.sw.entity.Payments;
import com.sw.repository.PaymentsRepository;
import org.springframework.stereotype.Service;

@Service
public class PaymentsService {
    private final PaymentsRepository paymentsRepository;
    public PaymentsService(PaymentsRepository paymentsRepository) {
        this.paymentsRepository = paymentsRepository;
    }
    public Payments save(Payments payments) {
        return paymentsRepository.save(payments);
    }
}

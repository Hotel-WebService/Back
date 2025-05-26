package com.sw.controller;

import com.sw.entity.Payments;
import com.sw.service.PaymentsService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

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
}

package com.sw.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

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
	
	 // 포트원 access_token 발급 예시 (실제 구현 필요)
    public String getPortoneAccessToken() {
        // 실제 구현에서는 포트원 REST API에 POST로 key/secret을 보내야 함
        // 예시용 하드코딩 (테스트 토큰)
        return "포트원_테스트_액세스토큰";
    }

    // 결제 취소
    public void cancelPayment(String impUid, String reason) {
        String accessToken = getPortoneAccessToken();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", accessToken);

        Map<String, Object> params = new HashMap<>();
        params.put("imp_uid", impUid);   // 포트원 결제 고유값
        params.put("reason", reason);    // 취소 사유

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(params, headers);
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> response = restTemplate.postForEntity(
            "https://api.iamport.kr/payments/cancel", entity, String.class
        );

        if (response.getStatusCode() == HttpStatus.OK) {
            System.out.println("결제 취소 성공");
        } else {
            System.out.println("결제 취소 실패: " + response.getBody());
            // 필요 시 예외처리 throw new RuntimeException 등
        }
    }
    
    @Transactional
    public void deleteByReservationId(Long reservationId) {
        paymentsRepository.deleteByReservationID(reservationId);
    }
}

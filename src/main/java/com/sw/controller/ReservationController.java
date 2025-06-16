package com.sw.controller;

import java.net.http.HttpResponse;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sw.entity.HotelUser;
import com.sw.entity.Reservation;
import com.sw.repository.HotelUserRepository;
import com.sw.repository.ReservationRepository;
import com.sw.service.PaymentsService;
import com.sw.service.ReservationService;
import com.sw.service.RoomQuantityService;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import com.jayway.jsonpath.JsonPath;


@RestController
@RequestMapping("/api/reservation")
@CrossOrigin(origins = "*")
public class ReservationController {

	@Autowired
	private ReservationRepository reservationRepo;

	private final RoomQuantityService roomQuantityService;

	@Autowired
	HotelUserRepository userRepo;

	@Autowired
	private PaymentsService paymentsService;

	private final ReservationService reservationService;

	public ReservationController(ReservationService reservationService, RoomQuantityService roomQuantityService) {
		this.reservationService = reservationService;
		this.roomQuantityService = roomQuantityService;
	}
	
	private static final String API_KEY = "2470831017523584";
    private static final String API_SECRET = "6IUZgxdGFlxJIii8g20O9ZB7N1lupPliSBl7ce5UIuJewbSdy52EpOap7rqzEZTuWUVotdHsIpwWO60l";

	@PostMapping
	public Reservation createReservation(@RequestBody Reservation reservation) {
		reservation.setStatus(Reservation.Status.Y);
		reservation.setReservationDate(LocalDateTime.now());
		return reservationService.save(reservation);
	}

	@GetMapping("/my")
	public List<Reservation> getMyReservations(@RequestParam Long hotelID, Principal principal) {
		// principal.getName()이 username일 경우
		HotelUser user = userRepo.findByLoginID(principal.getName()).orElseThrow(() -> new RuntimeException("사용자 없음"));
		Long userID = user.getUserID();
		return reservationRepo.findByUserIDAndHotelID(userID, hotelID);
	}

	@PostMapping("/cancelReservation")
	public String cancelReservation(@RequestParam String impUid, @RequestParam String reason) {
		// 예약 취소 로직(예약 상태 변경 등) 먼저 처리

		// 결제 취소 호출
		paymentsService.cancelPayment(impUid, reason);

		return "예약 및 결제 취소 완료";
	}

	@DeleteMapping("/{reservationID}")
	public void deleteById(@PathVariable Long reservationID) {
		// 1) 예약 정보 찾기
		Reservation reservation = reservationService.findById(reservationID);
		if (reservation != null) {
			// 2) 예약 상태를 취소로 변경(혹은 실제 삭제)
			reservation.setStatus(Reservation.Status.N);
			reservationService.save(reservation);

			// 3) 객실 재고 복구
			roomQuantityService.cancelRoomReserve(reservation.getRoomID(), reservation.getCheck_in_date(), 1);

			// 4) 해당 예약과 연결된 결제 row 업데이트
			paymentsService.updateStatusToNByReservationId(reservationID);
		//	paymentsService.deleteByReservationId(reservationID);

			// 5) 필요시 예약 row 자체도 삭제하고 싶다면 아래 코드 사용
		//	reservationService.deleteById(reservationID);
		}
	}
	
	@PostMapping("/cancel")
	public ResponseEntity<?> cancelReservationAndPayment(@RequestBody Map<String, Object> body) {
	    Long reservationID = Long.valueOf(body.get("reservationID").toString());
	    Long paymentID = body.get("paymentID") != null ? Long.valueOf(body.get("paymentID").toString()) : null;
	    String impUid = (String) body.get("imp_uid"); // ★ 프론트에서 imp_uid로 보낸 값

	    try {
	        // (1) 예약/결제 DB 상태 변경
	        Reservation reservation = reservationService.findById(reservationID);
	        if (reservation != null) {
	            reservation.setStatus(Reservation.Status.N);
	            reservationService.save(reservation);

	            roomQuantityService.cancelRoomReserve(reservation.getRoomID(), reservation.getCheck_in_date(), 1);
	            paymentsService.updateStatusToNByReservationId(reservationID);
	        }

	        // (2) 포트원 결제취소
	        boolean portoneResult = cancelPortonePayment(impUid);

	        return ResponseEntity.ok(Map.of("status", "success", "portone", portoneResult));
	    } catch (Exception e) {
	        return ResponseEntity.status(500).body(Map.of("status", "fail", "message", e.getMessage()));
	    }
	}
	// 결제취소 함수
    private boolean cancelPortonePayment(String impUid) throws Exception {
        // 1. 포트원 토큰 발급
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest tokenRequest = HttpRequest.newBuilder()
            .uri(URI.create("https://api.iamport.kr/users/getToken"))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(String.format(
                "{" +
                    "\"imp_key\": \"%s\"," +
                    "\"imp_secret\": \"%s\"" +
                "}", API_KEY, API_SECRET)))
            .build();
        HttpResponse<String> tokenResponse = client.send(tokenRequest, HttpResponse.BodyHandlers.ofString());
        String accessToken = JsonPath.read(tokenResponse.body(), "$.response.access_token");

        // 2. 결제취소 요청
        HttpRequest cancelRequest = HttpRequest.newBuilder()
            .uri(URI.create("https://api.iamport.kr/payments/cancel"))
            .header("Content-Type", "application/json")
            .header("Authorization", accessToken)
            .POST(HttpRequest.BodyPublishers.ofString(String.format(
                "{" +
                    "\"imp_uid\": \"%s\"" +
                "}", impUid)))
            .build();

        HttpResponse<String> cancelResponse = client.send(cancelRequest, HttpResponse.BodyHandlers.ofString());
        return cancelResponse.statusCode() == 200;
    }

}

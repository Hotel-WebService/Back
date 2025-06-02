package com.sw.controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

    @PostMapping
    public Reservation createReservation(@RequestBody Reservation reservation) {
        reservation.setStatus(Reservation.Status.예약완료);
        reservation.setReservationDate(LocalDateTime.now());
        return reservationService.save(reservation);
    }
    /*
    @GetMapping("/my")
    public List<Reservation> getMyReservations(@RequestParam Long hotelID, Principal principal) {
    	System.out.println("principal.getName(): " + principal.getName());
        Long userID = Long.parseLong(principal.getName()); // principal.getName()이 userID가 맞는지 확인
        return reservationRepo.findByUserIDAndHotelID(userID, hotelID);
    }*/
    
    @GetMapping("/my")
    public List<Reservation> getMyReservations(@RequestParam Long hotelID, Principal principal) {
        // principal.getName()이 username일 경우
        HotelUser user = userRepo.findByLoginID(principal.getName())
            .orElseThrow(() -> new RuntimeException("사용자 없음"));
        Long userID = user.getUserID();
        return reservationRepo.findByUserIDAndHotelID(userID, hotelID);
    }
    
    /*
	@DeleteMapping("/{reservationID}")
	public void deletePayment(@PathVariable Long paymentId) {
		paymentsService.deleteById(paymentId);
	}
    */
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
            reservation.setStatus(Reservation.Status.예약취소);
            reservationService.save(reservation);

            // 3) 객실 재고 복구
            roomQuantityService.cancelRoomReserve(
                reservation.getRoomID(),
                reservation.getCheck_in_date(),
                1
            );

            // 4) 해당 예약과 연결된 결제 row 삭제
            paymentsService.deleteByReservationId(reservationID);

            // 5) 필요시 예약 row 자체도 삭제하고 싶다면 아래 코드 사용
            reservationService.deleteById(reservationID);
        }
    }
    
    
}

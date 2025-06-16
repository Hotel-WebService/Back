package com.sw.scheduler;

import java.time.LocalDate;
import java.util.List;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.sw.entity.Reservation;
import com.sw.service.ReservationService;
import com.sw.service.RoomQuantityService;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RoomScheduler {

    private final ReservationService reservationService;
    private final RoomQuantityService roomQuantityService;

    // 매일 새벽 3시에 실행 (cron = "0 0 3 * * *")
    @Scheduled(cron = "0 0 3 * * *")
    public void restoreRoomsAfterCheckout() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        // 어제 체크아웃한 모든 예약 조회 (여기서 Status.Y == 체크아웃된 예약만)
        List<Reservation> checkedOut = reservationService.findByCheckOutDateAndStatus(yesterday, Reservation.Status.Y);
        for (Reservation reservation : checkedOut) {
            roomQuantityService.restoreRoomAfterCheckout(reservation.getRoomID(), reservation.getCheck_out_date(), 1);
        }
    }
}

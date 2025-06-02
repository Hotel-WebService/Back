package com.sw.service;

import java.time.LocalDate;
import org.springframework.stereotype.Service;
import com.sw.entity.RoomQuantity;
import com.sw.entity.RoomQuantityId;
import com.sw.repository.RoomQuantityRepository;
import com.sw.repository.RoomRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoomQuantityService {

    private final RoomQuantityRepository roomQuantityRepository;
    private final RoomRepository roomRepository;

    /**
     * 단순 재고 조회 (없으면 생성, reservedCount=0)
     */
    public RoomQuantity getOrCreateQuantity(Long roomID, LocalDate date) {
        return roomQuantityRepository.findByIdRoomIDAndIdDate(roomID, date)
            .orElseGet(() -> {
                int total = (int)(Math.random() * 5) + 2;
                RoomQuantity rq = RoomQuantity.builder()
                        .id(new RoomQuantityId(roomID, date))
                        .totalCount(total)
                        .reservedCount(0)  // 조회시에는 예약수 0
                        .build();
                return roomQuantityRepository.save(rq);
            });
    }

    /**
     * 예약(재고 감소)
     * - 이미 있으면 reservedCount 누적
     * - 없으면 totalCount 랜덤 생성 후 reservedCount에 count 적용
     * 실제 결제/예약 확정 단계에서만 호출!
     */
    public boolean reserveRoom(Long roomID, LocalDate date, int count) {
        RoomQuantity rq = roomQuantityRepository.findByIdRoomIDAndIdDate(roomID, date)
            .orElseGet(() -> {
                int total = (int)(Math.random() * 5) + 2; // 2~6 랜덤
                // 최초 생성시에도 reservedCount = count
                return roomQuantityRepository.save(RoomQuantity.builder()
                        .id(new RoomQuantityId(roomID, date))
                        .totalCount(total)
                        .reservedCount(0) // 0으로 생성, 아래에서 바로 count만큼 누적
                        .build());
            });
        // 예약 가능한 수량 확인
        if (rq.getAvailableCount() != null && rq.getAvailableCount() >= count) {
            rq.setReservedCount(rq.getReservedCount() + count);
            roomQuantityRepository.save(rq);
            return true;
        }
        // 재고 부족
        return false;
    }
    
    
    public boolean cancelRoomReserve(Long roomID, LocalDate date, int count) {
        // 기존 row 찾기 (없으면 아무것도 안함)
        return roomQuantityRepository.findByIdRoomIDAndIdDate(roomID, date)
            .map(rq -> {
                int now = rq.getReservedCount() - count;
                rq.setReservedCount(Math.max(0, now)); // 0 미만 방지
                roomQuantityRepository.save(rq);
                return true;
            }).orElse(false);
    }
    
    
}

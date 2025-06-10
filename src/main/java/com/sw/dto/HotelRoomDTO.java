package com.sw.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class HotelRoomDTO {
    private Long hotelID;
    private String hotelName;
    private String star;
    private String district;
    private String city;
    private Boolean parkingLot;
    private Long roomID;
    private Integer price;
    private Integer capacity;
    // 기타 필요 필드 추가 가능
}

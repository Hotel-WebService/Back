package com.sw.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor // JPA에서 DTO로 사용하려면 기본 생성자 필요
public class HotelCandidateDto {
    private Long hotelID;
    private String hotelName;
    private String description;
    private String address;
    private String city;
    private String district;
    private Double latitude;
    private Double longitude;
    private String star;
    private Boolean parkingLot;

    // Hibernate에서 new 키워드로 객체를 생성할 때 사용하는 생성자
    public HotelCandidateDto(Long hotelID, String hotelName, String description, String address,
                             String city, String district, Double latitude, Double longitude,
                             String star, Boolean parkingLot) {
        this.hotelID = hotelID;
        this.hotelName = hotelName;
        this.description = description;
        this.address = address;
        this.city = city;
        this.district = district;
        this.latitude = latitude;
        this.longitude = longitude;
        this.star = star;
        this.parkingLot = parkingLot;
    }
}

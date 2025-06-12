package com.sw.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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
}
 
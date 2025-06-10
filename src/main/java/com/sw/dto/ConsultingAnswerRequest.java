package com.sw.dto;

import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter
public class ConsultingAnswerRequest {
    private Long userId;
    private String district;
    private String star;
    private String parkingLot;
    private String capacity;
    private String price;
    private String checkIn;
    private String theme;
    private String mood;
    private String special;
}

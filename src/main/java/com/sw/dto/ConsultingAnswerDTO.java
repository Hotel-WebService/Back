package com.sw.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ConsultingAnswerDTO {
    private Long userId;
    private String district;
    private String star;
    private String parking_lot;
    private String capacity;
    private String price;
    private String check_in;
    private String theme;
    private String mood;
    private String special;
}

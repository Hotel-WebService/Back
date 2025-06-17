package com.sw.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AiRecommendRequest {
    public List<HotelCandidateDto> hotelCandidates;
    public String theme;
    public String mood;
    public String special;
    public String district;
}


/*
public class AiRecommendRequest {
    private List<Map<String, Object>> hotelCandidates;
    private String theme;
    private String mood;
    private String special;
}
*/ 
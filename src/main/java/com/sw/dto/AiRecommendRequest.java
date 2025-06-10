package com.sw.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class AiRecommendRequest {
    private List<Map<String, Object>> hotelCandidates;
    private String theme;
    private String mood;
    private String special;
}

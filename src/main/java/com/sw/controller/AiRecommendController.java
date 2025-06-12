
package com.sw.controller;

import com.sw.dto.AiRecommendRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/hotels") 
public class AiRecommendController {

    @PostMapping("/ai-recommend")
    public ResponseEntity<?> recommendByAI(@RequestBody AiRecommendRequest request) {
        Map<String, Object> result = new HashMap<>();
        result.put("bestHotel", request.getHotelCandidates() == null || request.getHotelCandidates().isEmpty()
                ? null : request.getHotelCandidates().get(0));
        result.put("message", "AI가 가장 적합하다고 판단한 호텔 추천 결과입니다.");
        return ResponseEntity.ok(result);
    }
}
 
 
/*
package com.sw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sw.dto.AiRecommendRequest;
import com.sw.service.AiRecommendService;

@RestController
@RequestMapping("/api/hotels")
public class AiRecommendController {

    @Autowired
    private AiRecommendService aiRecommendService;

    @PostMapping("/ai-recommend")
    public ResponseEntity<?> recommendByAI(@RequestBody AiRecommendRequest req) {
        return ResponseEntity.ok(aiRecommendService.getAiRecommendation(req));
    }
}

*/
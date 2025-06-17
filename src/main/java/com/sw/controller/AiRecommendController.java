/*
package com.sw.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sw.dto.AiRecommendRequest;
import com.sw.dto.HotelCandidateDto;

// 현재 테스트 코드 CHATGPT 미연동
@RestController
@RequestMapping("/api/hotels") 
public class AiRecommendController {

   @PostMapping("/ai-recommend")
    public ResponseEntity<?> recommendByAI(@RequestBody AiRecommendRequest request) {
        List<HotelCandidateDto> candidates = request.getHotelCandidates(); // 0615 수정

        Map<String, Object> result = new HashMap<>();

        if (candidates == null || candidates.isEmpty()) {
            result.put("recommendedHotels", null);
            result.put("message", "조건에 맞는 호텔이 없습니다.");
        } else {
            result.put("recommendedHotels", candidates); // 후보 전체 반환
            result.put("message", "AI 조건에 부합하는 호텔 리스트입니다.");
        }

        return ResponseEntity.ok(result);
    }
}
*/ 
 
/* 아래 주석 코드 실제 CHATGPT 연동코드
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


package com.sw.controller;

import com.sw.dto.AiRecommendRequest;
import com.sw.service.AiRecommendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/hotels")
public class AiRecommendController {

    @Autowired
    private AiRecommendService aiRecommendService; // ✅ 서비스 주입

    @PostMapping("/ai-recommend")
    public ResponseEntity<?> recommendByAI(@RequestBody AiRecommendRequest request) {
        // ✅ 서비스 메서드 호출하여 응답 그대로 반환
        return ResponseEntity.ok(aiRecommendService.getAiRecommendation(request));
    }
}
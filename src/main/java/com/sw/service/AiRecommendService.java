package com.sw.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sw.dto.AiRecommendRequest;
import com.sw.dto.HotelCandidateDto;
import com.sw.entity.Hotel;
import com.sw.repository.HotelRepository;

@Service
public class AiRecommendService {

    @Autowired
    private ChatGptService chatGptService;

    @Autowired
    private HotelRepository hotelRepository;

    public Map<String, Object> getAiRecommendation(AiRecommendRequest req) {
        List<HotelCandidateDto> candidates = req.getHotelCandidates(); // 요청값
        String theme = req.getTheme();
        String mood = req.getMood();
        String special = req.getSpecial();

        Map<String, Object> result = new HashMap<>();

        // 1️. 후보가 3개 이상일 경우 → 2~3개 추천
        if (candidates != null && candidates.size() >= 3) {
            String hotelList = formatHotelList(candidates);
            String prompt = String.format(
                """
                고객의 조건:
                - 테마: %s
                - 분위기: %s
                - 특별요구: %s

                아래 호텔 후보 중 조건에 가장 잘 맞는 호텔 2~3개를 추천해주세요.
                결과는 JSON 배열로 호텔 이름만 주세요. 예: ["호텔A", "호텔B"]

                호텔 목록:
                %s
                """, theme, mood, special, hotelList
            );

            String gptResp = chatGptService.askChatGpt(prompt);
            List<String> names = parseRecommendedNames(gptResp);

            List<HotelCandidateDto> recommended = candidates.stream()
                .filter(h -> names.contains(h.getHotelName()))
                .collect(Collectors.toList());

            result.put("recommendedHotels", recommended);
            result.put("message", "AI가 추천한 후보 호텔입니다.");
            return result;
        }

        // 2️. 후보가 1~2개일 경우 → 가장 적합한 1개 추천
        else if (candidates != null && candidates.size() >= 1 && candidates.size() <= 2) {
            String hotelList = formatHotelList(candidates);
            String prompt = String.format(
                """
                고객의 조건:
                - 테마: %s
                - 분위기: %s
                - 특별요구: %s

                아래 호텔 중에서 가장 적합한 호텔 하나만 골라주세요.
                결과는 JSON 배열로 호텔 이름만 주세요. 예: ["호텔B"]

                호텔 목록:
                %s
                """, theme, mood, special, hotelList
            );
            
            String gptResp = chatGptService.askChatGpt(prompt);
            List<String> names = parseRecommendedNames(gptResp);

            HotelCandidateDto best = candidates.stream()
                .filter(h -> names.contains(h.getHotelName()))
                .findFirst().orElse(null);

            result.put("bestHotel", best);
            result.put("message", "AI가 선택한 최적의 호텔입니다.");
            return result;
        }
     // 3️. 후보가 없을 경우 (candidates == null or 0개) → 지역 기반 추천!
        else {
    
        String district = req.getDistrict();
        if (district == null || district.isBlank()) {
            // district가 없으면 지역 추천 불가
            result.put("recommendedHotels", Collections.emptyList());
            result.put("message", "조건에도 맞지 않고 지역 정보도 없어 추천할 수 없습니다.");
            return result;
        }

        // 지역구 기준 호텔 목록 불러오기
        List<Hotel> hotels = hotelRepository.findByDistrict(district);
        if (hotels.isEmpty()) {
            result.put("recommendedHotels", Collections.emptyList());
            result.put("message", "해당 지역구에는 저장된 호텔목록이 없습니다.");
            return result;
        }

        // 엔티티 → DTO 변환
        List<HotelCandidateDto> fallback = hotels.stream()
            .map(h -> new HotelCandidateDto(
                    h.getHotelID(),
                    h.getHotelName(),
                    h.getDescription(),
                    h.getAddress(),
                    h.getCity(),
                    h.getDistrict(),
                    h.getLatitude() != null ? h.getLatitude().doubleValue() : null,
                    h.getLongitude() != null ? h.getLongitude().doubleValue() : null,
                    h.getStar(),
                    h.getParkingLot() != null && (h.getParkingLot() == true || h.getParkingLot().equals(true)) // Boolean 타입 처리
            ))
            .collect(Collectors.toList());

        String hotelList = formatHotelList(fallback);
        String prompt = String.format(
            """
            사용자가 '%s' 지역에서 호텔을 찾고 있습니다.
            아래는 해당 지역의 호텔 목록입니다.

            이 중 사용자에게 적절한 호텔 1~2개를 추천해주세요.
            결과는 JSON 배열로 호텔 이름만 주세요. 예: ["호텔A", "호텔B"]

            호텔 목록:
            %s
            """, district, hotelList
        );

        String gptResp = chatGptService.askChatGpt(prompt);
        List<String> names = parseRecommendedNames(gptResp);

        List<HotelCandidateDto> recommended = fallback.stream()
            .filter(h -> names.contains(h.getHotelName()))
            .collect(Collectors.toList());

        result.put("recommendedHotels", recommended);
        result.put("message", "조건에 맞는 호텔은 없지만 지역 기반 추천입니다.");
        return result;
    }
    }
    // 🔧 호텔 리스트 포맷팅
    private String formatHotelList(List<HotelCandidateDto> list) {
        return list.stream()
                .map(h -> String.format("호텔명: %s, 설명: %s",
                        h.getHotelName(), h.getDescription() != null ? h.getDescription() : ""))
                .collect(Collectors.joining("\n"));
    }

    // 🔧 GPT 응답에서 JSON 배열 파싱
    private List<String> parseRecommendedNames(String gptText) {
        try {
            String jsonPart = gptText.replaceAll(".*?(\\[.*?\\]).*", "$1");
            return new ObjectMapper().readValue(jsonPart, new TypeReference<>() {});
        } catch (Exception e) {
            System.err.println("GPT 응답 파싱 오류: " + e.getMessage());
            return Collections.emptyList();
        }
    }
}

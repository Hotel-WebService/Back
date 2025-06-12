package com.sw.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sw.dto.AiRecommendRequest;
import com.sw.dto.HotelCandidateDto;

@Service
public class AiRecommendService {

    @Autowired
    private ChatGptService chatGptService;

    public Map<String, Object> getAiRecommendation(AiRecommendRequest req) {
        // 1. 후보 호텔 없으면 바로 리턴
        if (req.hotelCandidates == null || req.hotelCandidates.isEmpty()) {
            return Map.of(
                "bestHotel", null,
                "message", "후보 호텔이 없습니다."
            );
        }

        // 2. 프롬프트 생성
        StringBuilder prompt = new StringBuilder();
        prompt.append("고객님의 조건: 테마=" + req.theme + ", 분위기=" + req.mood + ", 특별요구=" + req.special + "\n");
        prompt.append("후보 호텔 리스트:\n");
        for (int i = 0; i < req.hotelCandidates.size(); i++) {
            HotelCandidateDto h = req.hotelCandidates.get(i);
            prompt.append(String.format(
                "%d. %s (등급: %s, 지역: %s, 주소: %s)\n  설명: %s\n",
                i+1, h.getHotelName(), h.getStar(), h.getDistrict(), h.getAddress(), h.getDescription() != null ? h.getDescription().split("\n")[0] : ""
            ));
        }
        prompt.append(
            "\n이 호텔 리스트 중에서 고객님 조건에 가장 적합한 호텔 하나만 골라 호텔명만 한 줄로 반환하고, 그 다음 줄에 추천 이유를 적어주세요.\n"
            + "반드시 예시처럼:\n"
            + "코트야드 바이 메리어트 서울타임스퀘어\n"
            + "이 호텔은 가족 친화 시설과 모던한 분위기가 잘 갖추어져 있어서 추천합니다.\n"
        );

        // 3. ChatGPT 호출
        String aiResp = chatGptService.askChatGpt(prompt.toString());
        System.out.println("🔎 [ChatGPT 응답] : " + aiResp);

        // 4. 결과 파싱
        String[] lines = aiResp.split("\\n");
        String hotelName = lines.length > 0 ? lines[0].trim() : null;
        // 후보 중 이름 매칭 (간단히 포함 여부로)
        HotelCandidateDto best = req.hotelCandidates.stream()
            .filter(h -> h.getHotelName().contains(hotelName) || hotelName.contains(h.getHotelName()))
            .findFirst().orElse(null);

        // 5. 결과 반환
        Map<String, Object> result = new HashMap<>();
        result.put("bestHotel", best);
        result.put("message", aiResp);
        return result;
    }
}
  
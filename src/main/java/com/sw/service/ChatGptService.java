package com.sw.service;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Service
public class ChatGptService {

	@Value("${openai.api-key}")
    private String OPENAI_API_KEY;
    private static final String OPENAI_API_URL = "https://api.openai.com/v1/chat/completions";

    public String askChatGpt(String userPrompt) {
        OkHttpClient client = new OkHttpClient();

        // JSON 안전하게 생성!
        JSONObject reqBody = new JSONObject();
        reqBody.put("model", "gpt-3.5-turbo");
        JSONArray messages = new JSONArray();
        JSONObject msg = new JSONObject();
        msg.put("role", "user");
        msg.put("content", userPrompt);
        messages.put(msg);
        reqBody.put("messages", messages);

        RequestBody body = RequestBody.create(
            reqBody.toString(), 
            MediaType.parse("application/json")
        );

        Request request = new Request.Builder()
            .url(OPENAI_API_URL)
            .post(body)
            .addHeader("Authorization", "Bearer " + OPENAI_API_KEY)
            .addHeader("Content-Type", "application/json")
            .build();

        try (Response response = client.newCall(request).execute()) {
            String result = response.body().string();
            System.out.println("🔎 [ChatGPT API 응답 전체] : " + result);

            JSONObject jsonObj = new JSONObject(result);

            // 에러가 있을 경우 처리
            if (jsonObj.has("error")) {
                String errMsg = jsonObj.getJSONObject("error").optString("message", "Unknown error");
                return "ChatGPT API 에러: " + errMsg;
            }

            // 정상 응답 처리
            if (jsonObj.has("choices")) {
                String content = jsonObj
                    .getJSONArray("choices")
                    .getJSONObject(0)
                    .getJSONObject("message")
                    .getString("content");
                return content;
            } else {
                // choices 필드 없음
                return "ChatGPT API 호출 실패: 'choices' 필드가 없습니다. 응답내용: " + result;
            }

        } catch (Exception e) {
            return "ChatGPT API 호출 실패 (예외): " + e.getMessage();
        }
    }
} 

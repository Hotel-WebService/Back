package com.sw.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

import com.jayway.jsonpath.JsonPath;


@RestController
@RequestMapping("/api/pay")
public class PaymentController {

    private final String API_KEY = "2470831017523584";
    private final String API_SECRET = "6IUZgxdGFlxJIii8g20O9ZB7N1lupPliSBl7ce5UIuJewbSdy52EpOap7rqzEZTuWUVotdHsIpwWO60l";

    @PostMapping("/verify")
    public ResponseEntity<?> verifyPayment(@RequestBody Map<String, String> payload) throws Exception {
        String impUid = payload.get("imp_uid");

        // 1. 토큰 발급
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest tokenRequest = HttpRequest.newBuilder()
                .uri(URI.create("https://api.iamport.kr/users/getToken"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString("""
                    {
                      "imp_key": "%s",
                      "imp_secret": "%s"
                    }
                    """.formatted(API_KEY, API_SECRET)))
                .build();

        HttpResponse<String> tokenResponse = client.send(tokenRequest, HttpResponse.BodyHandlers.ofString());
        String accessToken = JsonPath.read(tokenResponse.body(), "$.response.access_token");

        // 2. 결제내역 검증
        HttpRequest verifyRequest = HttpRequest.newBuilder()
                .uri(URI.create("https://api.iamport.kr/payments/" + impUid))
                .header("Authorization", accessToken)
                .build();

        HttpResponse<String> verifyResponse = client.send(verifyRequest, HttpResponse.BodyHandlers.ofString());

        return ResponseEntity.ok(verifyResponse.body());
    }
}

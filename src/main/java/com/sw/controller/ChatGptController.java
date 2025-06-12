package com.sw.controller;

import com.sw.service.ChatGptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/chatgpt")
public class ChatGptController {

    @Autowired
    private ChatGptService chatGptService;

    @PostMapping("/ask")
    public Map<String, String> askChatGpt(@RequestBody Map<String, String> body) {
        String prompt = body.get("prompt");
        String answer = chatGptService.askChatGpt(prompt);
        return Map.of("answer", answer);
    }
}
 
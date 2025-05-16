package com.sw.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PayPageController {
    @GetMapping("/pay")
    public String payPage() {
        return "pay";
    }
}
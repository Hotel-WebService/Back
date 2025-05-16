package com.sw.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sw.dto.UserDto;
import com.sw.entity.HotelUser;
import com.sw.service.HotelService;
import com.sw.service.HotelUserService;

@Controller
public class PageController {

	 private final HotelUserService hotelUserService;
	 private final HotelService hotelService;
	 private final ObjectMapper mapper;
    
    
		public PageController(HotelUserService hotelUserService, HotelService hotelService, ObjectMapper mapper) {
			this.hotelUserService = hotelUserService;
			this.hotelService = hotelService;
			this.mapper = mapper;
		}
    
    /*
    public PageController(HotelUserService hotelUserService) {
        this.hotelUserService = hotelUserService;
    }
    
    public PageController(HotelService hotelService, ObjectMapper mapper) {
        this.hotelService = hotelService;
        this.mapper = mapper;
    }
    
    */
/*
    @Value("${naverClientId}")
    private String naverClientId;
    */
    // ──────────────────────────────────────
    // 1) 홈 / 첫 페이지 (인증 없이 접근 가능)
    /*
    @GetMapping({"/", "/firstpage"})
    public String firstPage() {
        return "firstPage";  // templates/firstPage.html
    }
    */
    @Value("${kakao.appkey}")
    private String kakaoAppKey;
    
    
    @GetMapping({"/", "/firstpage"})
    public String firstPage(Model model) throws JsonProcessingException {
        // 호텔 정보 JSON 으로 변환
        String hotelsJson = mapper.writeValueAsString(hotelService.findAll());
        model.addAttribute("hotelsJson", hotelsJson);
        model.addAttribute("kakaoAppKey", kakaoAppKey);
        return "firstPage";
    }
    
   
    // ──────────────────────────────────────
    // 2) 회원가입 폼 (인증 없이 접근 가능)
    @GetMapping("/signupPage")
    public String signupForm(Model model) {
        model.addAttribute("userDto", new UserDto());
        return "signupPage"; // templates/signupPage.html
    }

    // 3) 회원가입 처리 (POST)
    @PostMapping("/signup")
    public String signupSubmit(@ModelAttribute UserDto userDto) {
        hotelUserService.register(userDto);
        return "redirect:/firstpage";
    }

    // ──────────────────────────────────────
    // 4) 로그인 폼 (인증 없이 접근 가능)
    @GetMapping("/login")
    public String loginForm() {
        return "loginPage";  // templates/loginPage.html
    }

    
    /*@GetMapping({"/myPage"})
    public String myPage() {
        return "myPage";  // templates/myPage.html
    }*/
    
    @GetMapping("/myPage")
    public String myPage(Model model, @AuthenticationPrincipal UserDetails ud) {
        HotelUser user = hotelUserService.findByLoginId(ud.getUsername());
        model.addAttribute("user", user);
        return "myPage";
    }
    
    @GetMapping({"savedPage"})
    public String savedPage() {
        return "savedPage";  // templates/firstPage.html
    }
     
    @GetMapping("/reservationPage")
    public String reservationPage(Model model, @AuthenticationPrincipal UserDetails ud) {
        HotelUser user = hotelUserService.findByLoginId(ud.getUsername());
        model.addAttribute("user", user);
        return "reservationPage";
    }

}
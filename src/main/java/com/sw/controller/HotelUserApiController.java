package com.sw.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sw.dto.UserDto;
import com.sw.entity.HotelUser;
import com.sw.service.HotelUserService;

@RestController
@RequestMapping("/api/users")
public class HotelUserApiController {

	 private final HotelUserService hotelUserService;

	    public HotelUserApiController(HotelUserService hotelUserService) {
	        this.hotelUserService = hotelUserService;
	    }

	    @PostMapping("/signup")
	    public ResponseEntity<HotelUser> signUp(@RequestBody UserDto userDto) {
	        HotelUser saved = hotelUserService.register(userDto);
	        return new ResponseEntity<>(saved, HttpStatus.CREATED);
	    }
    
    
  
}
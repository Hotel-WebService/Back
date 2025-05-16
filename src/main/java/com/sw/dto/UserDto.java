package com.sw.dto;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {
    private String name;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;
    
    private String email;
    private String punNumber;
    private String loginID;
    private String loginPassword;

    // Getters & Setters
}
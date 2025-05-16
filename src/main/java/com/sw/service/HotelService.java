package com.sw.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sw.entity.Hotel;
import com.sw.repository.HotelRepository;

@Service
public class HotelService {

    private final HotelRepository hotelRepository;

    public HotelService(HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
    }

    public List<Hotel> findAll() {
        return hotelRepository.findAll();
    }

}
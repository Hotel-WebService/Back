package com.sw.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sw.entity.Hotel;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {
	
}
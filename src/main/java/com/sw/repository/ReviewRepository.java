package com.sw.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sw.entity.ReviewTable;

public interface ReviewRepository extends JpaRepository<ReviewTable, Long> {
	List<ReviewTable> findByHotelID(Long hotelID);
}
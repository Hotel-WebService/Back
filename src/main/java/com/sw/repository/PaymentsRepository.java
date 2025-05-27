package com.sw.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sw.entity.Payments;

public interface PaymentsRepository extends JpaRepository<Payments, Long> {
	   List<Payments> findByUserID(Long userID);
	}

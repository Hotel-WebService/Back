package com.sw.service;

import java.time.LocalDateTime;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.sw.dto.UserDto;
import com.sw.entity.HotelUser;
import com.sw.repository.HotelUserRepository;

@Service
public class HotelUserService {

	private final HotelUserRepository repo;
	private final PasswordEncoder passwordEncoder;

	public HotelUserService(HotelUserRepository repo, PasswordEncoder passwordEncoder) {
		this.repo = repo;
		this.passwordEncoder = passwordEncoder;
	}

	public HotelUser register(UserDto dto) {
		HotelUser user = new HotelUser();
		user.setName(dto.getName());
		user.setBirthday(dto.getBirthday());
		user.setEmail(dto.getEmail());
		user.setPunNumber(dto.getPunNumber());
		user.setLoginID(dto.getLoginID());
		user.setLoginPassword(passwordEncoder.encode(dto.getLoginPassword()));
		user.setSignUpDate(LocalDateTime.now());
		return repo.save(user);
	}

	public HotelUser findByLoginId(String loginId) {
		return repo.findByLoginID(loginId)
				.orElseThrow(() -> new UsernameNotFoundException("User not found with loginId: " + loginId));
	}

}
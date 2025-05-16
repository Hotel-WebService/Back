package com.sw.service;

import java.util.Collections;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.sw.entity.HotelUser;
import com.sw.repository.HotelUserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final HotelUserRepository repo;

    public CustomUserDetailsService(HotelUserRepository repo) {
        this.repo = repo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        HotelUser user = repo.findByLoginID(username)
            .orElseThrow(() -> new UsernameNotFoundException("LoginID not found: " + username));

        return org.springframework.security.core.userdetails.User.builder()
            .username(user.getLoginID())
            .password(user.getLoginPassword())
            .authorities(Collections.emptyList())  // 필요하면 권한 추가
            .build();
    }
}

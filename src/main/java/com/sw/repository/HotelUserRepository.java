package com.sw.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import com.sw.entity.HotelUser;

public interface HotelUserRepository extends JpaRepository<HotelUser, Long> {
	// loginID 칼럼으로 Optional<User> 조회
	Optional<HotelUser> findByLoginID(String loginID);

	Optional<HotelUser> findByName(String name);
	
	Optional<HotelUser> findByNameAndPunNumber(String name, String punNumber);
	
	Optional<HotelUser> findByLoginIDAndPunNumber(String loginID, String punNumber);
}
 
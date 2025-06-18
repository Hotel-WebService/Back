package com.sw.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sw.entity.HotelUser;
import com.sw.repository.HotelUserRepository;
import com.sw.service.SMSService;

@RestController
@RequestMapping("/api")
public class UserInfoController {

	private final HotelUserRepository hoteluserRepository;
	private final PasswordEncoder passwordEncoder;
	private final Map<String, String> smsStore = new ConcurrentHashMap<>();

	@Autowired
	public UserInfoController(HotelUserRepository hoteluserRepository, PasswordEncoder passwordEncoder) {
		this.hoteluserRepository = hoteluserRepository;
		this.passwordEncoder = passwordEncoder;
	}
	
	@Autowired
	private SMSService smsService;
	
	/** -------------------- 회원가입(Signup) 추가 -------------------- **/
	public static class SignupDto {
		public String name;
		public String loginID;
		public String loginPassword;
		public String punNumber;
		public String email;
		public String birthday; // "YYYY-MM-DD" 형식
	}

	@PostMapping("/signup")
	public ResponseEntity<?> signup(@RequestBody SignupDto dto) {
		// 1) 로그인ID 중복 체크
		Optional<HotelUser> exist = hoteluserRepository.findByLoginID(dto.loginID);
		if (exist.isPresent()) {
			return ResponseEntity.badRequest().body(Map.of("error", "이미 사용 중인 아이디입니다."));
		}

		// 2) 새 사용자 엔티티 생성
		HotelUser user = new HotelUser();
		user.setName(dto.name);
		user.setLoginID(dto.loginID);
		user.setLoginPassword(passwordEncoder.encode(dto.loginPassword));
		user.setPunNumber(dto.punNumber.replaceAll("-", ""));
		user.setEmail(dto.email);
		user.setBirthday(LocalDate.parse(dto.birthday));
		user.setSignUpDate(LocalDateTime.now());

		hoteluserRepository.save(user);

		return ResponseEntity.ok(Map.of("status", "success"));
	}

	@GetMapping("/userinfo")
	public ResponseEntity<Map<String, Object>> userinfo(Authentication auth) {
		Map<String, Object> result = new HashMap<>();

		boolean isAuth = auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken);

		result.put("authenticated", isAuth);

		if (isAuth) {
			String loginID = auth.getName();
			Optional<HotelUser> userOpt = hoteluserRepository.findByLoginID(loginID);
			if (userOpt.isPresent()) {
				HotelUser user = userOpt.get();
				result.put("userID", user.getUserID());
				result.put("name", user.getName());
				result.put("birthday", user.getBirthday());
				result.put("email", user.getEmail());
				result.put("punNumber", user.getPunNumber());
				result.put("signUpDate", user.getSignUpDate());
				result.put("loginID", user.getLoginID());
				// 비밀번호 테스트 상 출력
				result.put("loginPassword", user.getLoginPassword());
			}
		}

		return ResponseEntity.ok(result);
	}

	/** 추가: 클라이언트가 보낼 JSON 바인딩용 DTO **/
	public static class UpdateUserDto {
		public String name;
		public String email;
		public String punNumber;
		public String loginPassword;
	}

	/** 추가: 사용자 정보 수정용 PUT 엔드포인트 **/
	@PutMapping("/userinfo")
	public ResponseEntity<?> updateUserinfo(@RequestBody UpdateUserDto dto, Authentication auth) {
		// 인증 여부 확인
		if (auth == null || !auth.isAuthenticated() || auth instanceof AnonymousAuthenticationToken) {
			return ResponseEntity.status(401).build();
		}

		// 현재 로그인된 ID로 사용자 조회
		String loginID = auth.getName();
		HotelUser user = hoteluserRepository.findByLoginID(loginID)
				.orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));

		// 수정 가능한 필드만 업데이트
		user.setName(dto.name);
		user.setEmail(dto.email);
		user.setPunNumber(dto.punNumber);

		// 비밀번호는 빈 문자열이 아닐 때만 변경
		if (dto.loginPassword != null && !dto.loginPassword.isBlank()) {
			user.setLoginPassword(passwordEncoder.encode(dto.loginPassword));
		}

		hoteluserRepository.save(user);

		Map<String, String> resp = new HashMap<>();
		resp.put("status", "success");
		return ResponseEntity.ok(resp);
	}

	// 아이디 중복 체크용
	@GetMapping("/check-id")
	public ResponseEntity<?> checkID(@RequestParam String loginID) {
		boolean exists = hoteluserRepository.findByLoginID(loginID).isPresent();
		return ResponseEntity.ok(Map.of("available", !exists));
	}
	
	@GetMapping("/find-id-by-phone")
	public ResponseEntity<String> findLoginIdByPhone(@RequestParam String name, @RequestParam String phone) {
	    Optional<HotelUser> user = hoteluserRepository.findByNameAndPunNumber(name, phone);
	    return user.map(value -> ResponseEntity.ok(value.getLoginID()))
	               .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body("일치하는 사용자가 없습니다."));
	}
	
	@GetMapping("/find-password")
	public ResponseEntity<?> verifyUserForPasswordReset(@RequestParam String loginID, @RequestParam String phone) {
	    Optional<HotelUser> user = hoteluserRepository.findByLoginIDAndPunNumber(loginID, phone);
	    return user.isPresent()
	        ? ResponseEntity.ok().build()
	        : ResponseEntity.status(HttpStatus.NOT_FOUND).body("일치하는 사용자가 없습니다.");
	}
	
	@PutMapping("/reset-password")
	public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> body) {
	    String loginID = body.get("loginID");
	    String newPassword = body.get("newPassword");

	    Optional<HotelUser> userOpt = hoteluserRepository.findByLoginID(loginID);
	    if (userOpt.isPresent()) {
	        HotelUser user = userOpt.get();
	        user.setLoginPassword(passwordEncoder.encode(newPassword));
	        hoteluserRepository.save(user);
	        return ResponseEntity.ok(Map.of("status", "비밀번호 변경 완료"));
	    }
	    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("사용자 없음");
	}

	@PostMapping("/auth/request-sms")
	public ResponseEntity<?> sendSms(@RequestBody Map<String, String> body) {
	    String phoneNumber = body.get("phoneNumber");
	    String code = smsService.sendVerificationCode(phoneNumber);
	    smsStore.put(phoneNumber, code);
	    return ResponseEntity.ok().build();
	}

	@PostMapping("/auth/verify-sms")
	public ResponseEntity<?> verifySms(@RequestBody Map<String, String> body) {
	    String phoneNumber = body.get("phoneNumber");
	    String inputCode = body.get("code");
	    String storedCode = smsStore.get(phoneNumber);
	    if (storedCode != null && storedCode.equals(inputCode)) {
	        return ResponseEntity.ok().build();
	    } else {
	        return ResponseEntity.status(401).body("인증 실패");
	    }
	}

}

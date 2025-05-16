package com.sw.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sw.entity.HotelUser;
import com.sw.repository.HotelUserRepository;

@RestController
@RequestMapping("/api")
public class UserInfoController {

    private final HotelUserRepository hoteluserRepository;
    private final PasswordEncoder      passwordEncoder;

    @Autowired
    public UserInfoController(HotelUserRepository hoteluserRepository,
                              PasswordEncoder passwordEncoder) {
        this.hoteluserRepository = hoteluserRepository;
        this.passwordEncoder      = passwordEncoder;
    }

    
    /**-------------------- 회원가입(Signup) 추가 --------------------**/
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
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("error", "이미 사용 중인 아이디입니다."));
        }

        // 2) 새 사용자 엔티티 생성
        HotelUser user = new HotelUser();
        user.setName(dto.name);
        user.setLoginID(dto.loginID);
        user.setLoginPassword(passwordEncoder.encode(dto.loginPassword));
        user.setPunNumber(dto.punNumber);
        user.setEmail(dto.email);
        user.setBirthday(LocalDate.parse(dto.birthday));
        user.setSignUpDate(LocalDateTime.now());

        hoteluserRepository.save(user);

        return ResponseEntity.ok(Map.of("status", "success"));
    }
    
 
    @GetMapping("/userinfo")
    public ResponseEntity<Map<String, Object>> userinfo(Authentication auth) {
        Map<String, Object> result = new HashMap<>();

        boolean isAuth = auth != null
                      && auth.isAuthenticated()
                      && !(auth instanceof AnonymousAuthenticationToken);

        result.put("authenticated", isAuth);

        if (isAuth) {
            String loginID = auth.getName();
            Optional<HotelUser> userOpt = hoteluserRepository.findByLoginID(loginID);
            if (userOpt.isPresent()) {
            	HotelUser user = userOpt.get();
                result.put("userID",       user.getUserID());
                result.put("name",         user.getName());
                result.put("birthday",     user.getBirthday());
                result.put("email",        user.getEmail());
                result.put("punNumber",    user.getPunNumber());
                result.put("signUpDate",   user.getSignUpDate());
                result.put("loginID",      user.getLoginID());
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
    public ResponseEntity<?> updateUserinfo(@RequestBody UpdateUserDto dto,
                                            Authentication auth) {
        // 인증 여부 확인
        if (auth == null
            || !auth.isAuthenticated()
            || auth instanceof AnonymousAuthenticationToken) {
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

        Map<String,String> resp = new HashMap<>();
        resp.put("status", "success");
        return ResponseEntity.ok(resp);
    }
    
}

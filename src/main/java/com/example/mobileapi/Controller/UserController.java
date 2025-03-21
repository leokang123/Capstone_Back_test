package com.example.mobileapi.Controller;

import com.example.mobileapi.DTO.LoginRequest;
import com.example.mobileapi.DTO.LoginResponse;
import com.example.mobileapi.DTO.RegisterRequest;
import com.example.mobileapi.DTO.RegisterResponse;
import com.example.mobileapi.Entity.Beacon;
import com.example.mobileapi.Entity.Role;
import com.example.mobileapi.Entity.User;
import com.example.mobileapi.Repository.UserRepository;
import com.example.mobileapi.Service.RefreshTokenService;
import com.example.mobileapi.Service.UserService;
import com.example.mobileapi.Utils.JwtUtil;
import com.example.mobileapi.Utils.Utils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@CrossOrigin(origins = "*")  // ✅ 모든 출처 허용 (필요 시 특정 도메인만 허용 가능)
@RequestMapping(("/auth"))
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;

    @Autowired
    public UserController(UserService userService, RefreshTokenService refreshTokenService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.refreshTokenService = refreshTokenService;
        this.jwtUtil = jwtUtil;

    }
    private String resolveToken(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
        return (bearer != null && bearer.startsWith("Bearer ")) ? bearer.substring(7) : null;
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(HttpServletRequest request) {
        String refreshToken = resolveToken(request);

        if (refreshToken == null || !jwtUtil.validateToken(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Refresh token이 유효하지 않습니다.");
        }

        String userId = jwtUtil.getUserIdFromToken(refreshToken);

        // ✅ Redis 등에서 저장된 refreshToken이 실제로 유효한지 비교
        if (!refreshTokenService.isValid(userId, refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("저장된 Refresh token과 일치하지 않습니다.");
        }

        // ✅ 새 accessToken 발급
        String newAccessToken = jwtUtil.generateAccessToken(userId);

        return ResponseEntity.ok(Map.of("accessToken", newAccessToken));
    }

    @PostMapping("/signin")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest userinfo) {
        log.info("Login request received");
        Optional<User> _user = userService.login(userinfo.getUsername(), userinfo.getPassword());
        if (_user.isPresent()) {
            User user = _user.get();
            String userId = user.getId().toString();
            String accessToken = jwtUtil.generateAccessToken(userId);
            String refreshToken = jwtUtil.generateRefreshToken(userId);
            refreshTokenService.saveRefreshToken(userId, refreshToken);
            return ResponseEntity.ok(new LoginResponse(user, accessToken, refreshToken));
        }
        return ResponseEntity.badRequest().body(new LoginResponse(null, null, null));
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest userinfo) {
        Utils util = new Utils();
        User newUser = new User();
        Long roleId = userinfo.getRoleId();

        newUser.setUserName(userinfo.getUsername());
        newUser.setName(userinfo.getName());
        newUser.setPassword(userinfo.getPassword());
        newUser.setEmail(userinfo.getEmail());
        newUser.setPhoneNumber(userinfo.getPhoneNumber());
        newUser.setSelectedHospital(userinfo.getSelectedHospital());
        newUser.setRole(userService.getRole(roleId));
        log.info(newUser.getUserName());
        try {
            userService.save(newUser);
        } catch(Exception e) {
            String errorMessage = util.extractDetailMessage(e.getMessage());
            log.error(errorMessage);
            log.error(e.getMessage());

            return ResponseEntity.badRequest().body(new RegisterResponse(errorMessage));
        }
        return ResponseEntity.ok(new RegisterResponse("User registered successfully"));
    }

    @PostMapping("/save_beacon")
    public ResponseEntity<RegisterResponse> saveBeacon(@RequestBody Beacon beacon) {
        try{
            userService.saveBeacon(beacon);
        } catch(Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body(new RegisterResponse(e.getMessage()));
        }
        return ResponseEntity.ok(new RegisterResponse("Beacon saved successfully"));
    }

    @PostMapping("/save_beacons")
    public ResponseEntity<RegisterResponse> saveBeacons(@RequestBody List<Beacon> beacons) {
        try {
            userService.saveBeacons(beacons); // 리스트로 저장
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body(new RegisterResponse(e.getMessage()));
        }
        return ResponseEntity.ok(new RegisterResponse("All beacons saved successfully"));
    }

    @PostMapping("/save_roles")
    public ResponseEntity<RegisterResponse> saveRoles(@RequestBody List<Role> roles) {
        try {
            userService.saveRoles(roles);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body(new RegisterResponse(e.getMessage()));
        }
        return ResponseEntity.ok(new RegisterResponse("All roles saved successfully"));
    }

}



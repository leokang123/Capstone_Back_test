package com.example.mobileapi.Service;

import com.example.mobileapi.Entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserService userService;

    @Autowired
    public CustomUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        throw new UnsupportedOperationException("loadUserByUsername() is not used. Use loadUserByUserId() instead.");
    }

    public UserDetails loadUserByUserId(String _userId) throws UsernameNotFoundException {
        Long userId = Long.parseLong(_userId);
        User user = userService.findByUserId(userId)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        return new org.springframework.security.core.userdetails.User(
                user.getUserName(), "",  // 패스워드 필요 없음 (JWT 인증만 사용한다면)
                List.of(new SimpleGrantedAuthority("ROLE_USER")) // 권한 필요시 설정
        );
    }
}

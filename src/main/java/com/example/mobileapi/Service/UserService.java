package com.example.mobileapi.Service;

import com.example.mobileapi.Entity.Beacon;
import com.example.mobileapi.Entity.Role;
import com.example.mobileapi.Entity.User;
import com.example.mobileapi.Entity.WasteStorage;
import com.example.mobileapi.Repository.BeaconRepository;
import com.example.mobileapi.Repository.RoleRepository;
import com.example.mobileapi.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.passwordEncoder = new BCryptPasswordEncoder();
        this.userRepository = userRepository;
    }

    public void save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public Optional<User> findByUserId(Long userId) {
        return userRepository.findById(userId);
    }

    public Optional<User> login(String username, String rawPassword) {
        return userRepository.findByUserName(username)
                .filter(user -> passwordEncoder.matches(rawPassword, user.getPassword()));
    }


}

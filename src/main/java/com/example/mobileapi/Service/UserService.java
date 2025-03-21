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
    private final BeaconRepository beaconRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository,
                       BeaconRepository beaconRepository,
                       RoleRepository roleRepository) {
        this.passwordEncoder = new BCryptPasswordEncoder();
        this.userRepository = userRepository;
        this.beaconRepository = beaconRepository;
        this.roleRepository = roleRepository;
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

    public void saveBeacon(Beacon beacon) {
        beaconRepository.save(beacon);
    }

    // 저장소 리스트형태로 다저장
    public void saveBeacons(List<Beacon> beacons) {
        beaconRepository.saveAll(beacons); // 한 번에 저장
    }

    public void saveRoles(List<Role> roles) {
        roleRepository.saveAll(roles);
    }

    public Role getRole(Long roleId) {
        return roleRepository.findById(roleId).orElse(null);
    }
}

package com.dropbox.main.service;

import com.dropbox.main.config.web.UserRegistrationDto;
import com.dropbox.main.model.Role;
import com.dropbox.main.model.User;
import com.dropbox.main.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public int[] getIdsByEmail(Set<String> emails) {
        return userRepository.findIdByEmail(emails);
    }

    public User save(UserRegistrationDto registrationDto) {
        User user = new User(registrationDto.getName(),
                registrationDto.getEmail(), passwordEncoder.encode(registrationDto.getPassword()),
                List.of(new Role("ROLE_USER")));
        return userRepository.save(user);
    }
}
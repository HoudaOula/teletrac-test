package com.houdaoul.teletractest.service;

import com.houdaoul.teletractest.domain.Authority;
import com.houdaoul.teletractest.domain.User;
import com.houdaoul.teletractest.domain.dto.UserDto;
import com.houdaoul.teletractest.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<User> saveUser(UserDto userDto) throws Exception {
        if (!userRepository.findById(userDto.getUsername()).isEmpty()) {
            logger.error("User " + userDto.getUsername() + " already exists!");
            throw new Exception();
        }

        User user = User.builder()
                .username(userDto.getUsername())
                .password(encryptPassword(userDto.getPassword()))
                .authorities(getUserRoles())
                .build();

        return Optional.of(userRepository.save(user));
    }

    public Optional<User> saveAdmin(UserDto userDto) throws Exception {
        if (!getUserByUsername(userDto.getUsername()).isEmpty()) {
            logger.error("User " + userDto.getUsername() + " already exists!");
            throw new Exception();
        }

        User admin = User.builder()
                .username(userDto.getUsername())
                .password(encryptPassword(userDto.getPassword()))
                .authorities(getAdminRoles())
                .build();

        return Optional.of(userRepository.save(admin));
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findById(username);
    }

    public List<User> findAllUsers() {
        List<User> users = new ArrayList<>();
        userRepository.findAll().forEach(user -> users.add(user));
        return users;
    }

    public List<Authority> getUserRoles() {
        List<Authority> authorities = new ArrayList<>();
        authorities.add(new Authority("USER"));
        return authorities;
    }

    public List<Authority> getAdminRoles() {
        List<Authority> authorities = new ArrayList<>();
        authorities.add(new Authority("ADMIN"));
        authorities.add(new Authority("USER"));
        return authorities;
    }

    public boolean checkPassword(User user, String password) {
        return passwordEncoder.matches(password, user.getPassword());
    }

    public String encryptPassword(String password) {
        return passwordEncoder.encode(password);
    }
}

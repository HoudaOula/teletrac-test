package com.houdaoul.teletractest.controller;

import com.houdaoul.teletractest.config.JwtTokenProvider;
import com.houdaoul.teletractest.domain.User;
import com.houdaoul.teletractest.domain.dto.UserDto;
import com.houdaoul.teletractest.service.UserService;
import com.houdaoul.teletractest.util.LoginResponse;
import com.houdaoul.teletractest.util.Tokenizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;


@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    private final UserService userService;

    private final Tokenizer tokenizer;

    private final JwtTokenProvider jwtTokenProvider;

    public AuthenticationController(UserService userService, Tokenizer tokenizer, JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.tokenizer = tokenizer;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDto userLoginRequest) {
        if (userLoginRequest.getUsername() == null || userLoginRequest.getPassword() == null) {
            var loginResponse = LoginResponse.builder()
                    .message("Username and password required")
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(loginResponse);
        }

        Optional<User> maybeUser = userService.getUserByUsername(userLoginRequest.getUsername());
        if (maybeUser.isEmpty()) {
            var loginResponse = LoginResponse.builder()
                    .message("Username doesn't exist")
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(loginResponse);
        }
        User curUser = maybeUser.get();
        if (!userService.checkPassword(curUser, userLoginRequest.getPassword())) {
            var loginResponse = LoginResponse.builder()
                    .message("Incorrect password")
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(loginResponse);
        }

        //String token = tokenizer.generateToken(curUser);
        String token = jwtTokenProvider.createToken(curUser);

        logger.info("User [" + curUser.getUsername() + "] logged in!");

        var loginResponse = LoginResponse.builder()
                .message("Logged in")
                .token(token)
                .build();

        return ResponseEntity.ok().body(loginResponse);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestBody /*todo: validate user*/ UserDto userRegisterRequest)
            throws Exception {
        UserDto user = UserDto.builder()
                .username(userRegisterRequest.getUsername())
                .password(userRegisterRequest.getPassword())
                .build();

        userService.saveUser(user);

        return ResponseEntity.ok().build();
    }
}

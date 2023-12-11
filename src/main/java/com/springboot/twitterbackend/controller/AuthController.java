package com.springboot.twitterbackend.controller;

import com.springboot.twitterbackend.payload.JWTAuthResponse;
import com.springboot.twitterbackend.payload.LoginDto;
import com.springboot.twitterbackend.payload.LoginResponseDto;
import com.springboot.twitterbackend.payload.RegisterDto;
import com.springboot.twitterbackend.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@CrossOrigin(origins = "http://localhost:4200/")
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping(value = {"/accessToken"})
    public ResponseEntity<JWTAuthResponse> login(@RequestBody LoginDto loginDto) {

        String token = authService.login(loginDto);
        JWTAuthResponse jwtAuthResponse = new JWTAuthResponse();
        jwtAuthResponse.setAccessToken(token);
        return ResponseEntity.ok(jwtAuthResponse);
    }

    @PostMapping(value = {"/register"})
    public ResponseEntity<RegisterDto> register(@RequestBody RegisterDto registerDto) {
        try {
            logger.info("Request received at yourEndpoint");
            return new ResponseEntity<>(authService.register(registerDto), HttpStatus.CREATED);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = {"/login"})
    public ResponseEntity<LoginResponseDto> userLogin(@RequestBody LoginDto loginDto) {

        return new ResponseEntity<>(authService.userLogin(loginDto), HttpStatus.OK);
    }


}
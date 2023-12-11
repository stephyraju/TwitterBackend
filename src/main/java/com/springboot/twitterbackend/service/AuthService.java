package com.springboot.twitterbackend.service;


import com.springboot.twitterbackend.payload.LoginDto;
import com.springboot.twitterbackend.payload.LoginResponseDto;
import com.springboot.twitterbackend.payload.RegisterDto;

public interface AuthService {
    String login(LoginDto loginDto);

    RegisterDto register(RegisterDto registerDto);

    LoginResponseDto userLogin(LoginDto loginDto);
}

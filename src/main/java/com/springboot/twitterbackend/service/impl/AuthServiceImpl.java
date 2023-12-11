package com.springboot.twitterbackend.service.impl;

import com.springboot.twitterbackend.entity.Role;
import com.springboot.twitterbackend.entity.User;
import com.springboot.twitterbackend.exception.BlogAPIException;
import com.springboot.twitterbackend.payload.LoginDto;
import com.springboot.twitterbackend.payload.LoginResponseDto;
import com.springboot.twitterbackend.payload.RegisterDto;
import com.springboot.twitterbackend.repository.RoleRepository;
import com.springboot.twitterbackend.repository.UserRepository;
import com.springboot.twitterbackend.security.JwtTokenProvider;
import com.springboot.twitterbackend.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class AuthServiceImpl implements AuthService {

    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private JwtTokenProvider jwtTokenProvider;


    public AuthServiceImpl(AuthenticationManager authenticationManager,
                           UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder,
                           JwtTokenProvider jwtTokenProvider

    ) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;

   }

    @Override
    public String login(LoginDto loginDto) {

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getEmail(), loginDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtTokenProvider.generateToken(authentication);

        return token;
    }


    @Override
    public RegisterDto register(RegisterDto registerDto) {


        String regex = "^(.+)@(.+)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(registerDto.getEmail());
        if(matcher.matches()!=true) {
            System.out.println(registerDto.getEmail() +" : "+ matcher.matches());
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Please provide valid email!.");
        }


        // add check for email exists in database
        if(userRepository.existsByEmail(registerDto.getEmail())){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Email is already exists!.");
        }

        User user = new User();
        user.setName(registerDto.getName());
        // user.setUsername(registerDto.getUsername());
        user.setEmail(registerDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        user.setUsername(registerDto.getUsername());
        user.setFollowing((long) 0);

        userRepository.save(user);
        String registrationMsg =  "User registered successfully!.";

        registerDto.setRegistrationMsg(registrationMsg);
        return registerDto;
    }

    @Override
    public LoginResponseDto userLogin(LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getEmail(), loginDto.getPassword()));

        LoginResponseDto loginResponseDto = new LoginResponseDto();
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtTokenProvider.generateToken(authentication);

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {


            String username = ((UserDetails)principal).getUsername();

            Long userId = userRepository.findUseridByEmail(username);
            loginResponseDto.setId(userId);
            loginResponseDto.setToken(token);


        } else {
            String username = principal.toString();
            System.out.println(username);
        }

        return loginResponseDto;

    }
}
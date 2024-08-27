package org.example.ted.controllers;

import org.example.ted.dto.AuthResponseDto;
import org.example.ted.dto.LoginDto;
import org.example.ted.dto.RegisterDto;
import org.example.ted.models.Role;
import org.example.ted.models.User;
import org.example.ted.models.UserAdvertisementMatrix;
import org.example.ted.repositories.AdvertisementRepository;
import org.example.ted.repositories.RoleRepository;
import org.example.ted.repositories.UserAdvertisementMatrixRepository;
import org.example.ted.repositories.UserRepository;
import org.example.ted.security.JWTGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Objects;

@RestController
@RequestMapping("/ted/auth")
public class AuthController {
    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private JWTGenerator jwtGenerator;
    private AdvertisementRepository advertisementRepository;
    private UserAdvertisementMatrixRepository userAdvertisementMatrixRepository;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, UserRepository userRepository,
                          PasswordEncoder passwordEncoder, JWTGenerator jwtGenerator, RoleRepository roleRepository, UserAdvertisementMatrixRepository userAdvertisementMatrixRepository) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtGenerator = jwtGenerator;
        this.roleRepository = roleRepository;
        this.userAdvertisementMatrixRepository = userAdvertisementMatrixRepository;
    }

    @PostMapping("login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtGenerator.generateJWT(authentication);
        return new ResponseEntity<>(new AuthResponseDto(token), HttpStatus.OK);
    }

    @PostMapping("register")
    public ResponseEntity<String> register(@RequestBody RegisterDto registerDto) {
        if(userRepository.existsByEmail(registerDto.getEmail())) {
            return new ResponseEntity<>("email already in use", HttpStatus.BAD_REQUEST);
        }
        if(!Objects.equals(registerDto.getPassword(), registerDto.getConfirmPassword())) {
            return new ResponseEntity<>("password confirmation error", HttpStatus.BAD_REQUEST);
        }
        User user = new User(registerDto.getEmail(), passwordEncoder.encode(registerDto.getPassword()),
                registerDto.getName(), registerDto.getSurname(), registerDto.getPhone());

        Role roles = roleRepository.findByName("USER").get();
//        user.setRoles(Collections.singletonList(roles));
        user.setRoles(Collections.singleton(roles));

        userRepository.save(user);
        for (int i = 1; i <= advertisementRepository.count(); i++) {
            userAdvertisementMatrixRepository.save(new UserAdvertisementMatrix(user, advertisementRepository.findById((long) i)));

        }
        return new ResponseEntity<>("account created successfully", HttpStatus.CREATED);
    }
}

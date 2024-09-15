package org.example.ted.controllers;

import org.example.ted.dto.AuthResponseDto;
import org.example.ted.dto.LoginDto;
import org.example.ted.dto.RegisterDto;
import org.example.ted.models.Role;
import org.example.ted.models.User;
import org.example.ted.repositories.RoleRepository;
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
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Objects;

@RestController
@RequestMapping("/ted/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTGenerator jwtGenerator;

    private static final String UPLOAD_FOLDER = System.getProperty("user.dir") + "/src/main/resources/temp";

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, UserRepository userRepository,
                          PasswordEncoder passwordEncoder, JWTGenerator jwtGenerator, RoleRepository roleRepository) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtGenerator = jwtGenerator;
        this.roleRepository = roleRepository;
    }

    @PostMapping("login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtGenerator.generateJWT(authentication);
        String email = jwtGenerator.getEmailFromJWT(token);
        User user = userRepository.findByEmail(email).orElse(null);
        assert user != null;
        long userId = user.getId();
        return new ResponseEntity<>(new AuthResponseDto(token, userId), HttpStatus.OK);
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
        user.setRoles(Collections.singleton(roles));

        // set user image
        Path path = Paths.get(UPLOAD_FOLDER + "/" + "userPhoto.jpg");
        user.setImagePath(path.toString());

        userRepository.save(user);
        return new ResponseEntity<>("account created successfully", HttpStatus.CREATED);
    }
}

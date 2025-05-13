package com.example.airticketsales.service.security.impl;

import com.example.airticketsales.dto.security.AuthResponse;
import com.example.airticketsales.dto.security.LoginDto;
import com.example.airticketsales.dto.security.RegisterDto;
import com.example.airticketsales.enums.UserRole;
import com.example.airticketsales.exception.NotFoundException;
import com.example.airticketsales.repository.UserRepository;
import com.example.airticketsales.service.security.AuthService;
import com.example.airticketsales.service.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import com.example.airticketsales.entity.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthResponse register(RegisterDto request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("İstifadəçi adı artıq mövcuddur");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email artıq mövcuddur");
        }

        var user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setRole(request.getRole() != null ? request.getRole() : UserRole.USER);

        userRepository.save(user);

        String jwt = jwtService.generateToken(user);
        return new AuthResponse(jwt);
    }

    @Override
    public AuthResponse login(LoginDto request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );
        } catch (AuthenticationException e) {
            throw new RuntimeException("Yanlış istifadəçi adı və ya şifrə");
        }

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new NotFoundException("İstifadəçi tapılmadı"));

        String jwt = jwtService.generateToken(user);
        return new AuthResponse(jwt);
    }
}

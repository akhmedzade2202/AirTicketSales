package com.example.airticketsales.service.security;

import com.example.airticketsales.dto.security.AuthResponse;
import com.example.airticketsales.dto.security.LoginDto;
import com.example.airticketsales.dto.security.RegisterDto;

public interface AuthService {
    AuthResponse register(RegisterDto request);
    AuthResponse login(LoginDto request);
}

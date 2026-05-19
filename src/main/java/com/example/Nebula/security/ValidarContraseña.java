package com.example.Nebula.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class ValidarContraseña {

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public boolean isValid(String password) {
        return password != null && password.length() >= 6;
    }

    public String encode(String password) {
        return encoder.encode(password);
    }

    public boolean matches(String rawPassword, String encodedPassword) {
        return encoder.matches(rawPassword, encodedPassword);
    }
}
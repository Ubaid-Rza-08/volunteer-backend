package com.ubaid.volunteer_management;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) {
        try {
            // Check if user already exists
            if (userRepository.existsByEmail(request.getEmail())) {
                return new AuthResponse("Email is already registered!");
            }

            // Create new user
            User user = new User();
            user.setFirstName(request.getFirstName());
            user.setLastName(request.getLastName());
            user.setEmail(request.getEmail());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setPhoneNumber(request.getPhoneNumber());
            user.setRole(request.getRole());
            user.setEnabled(true);

            User savedUser = userRepository.save(user);

            // Generate JWT token
            Map<String, Object> extraClaims = new HashMap<>();
            extraClaims.put("role", savedUser.getRole().name());
            extraClaims.put("userId", savedUser.getId());

            String token = jwtUtil.generateToken(savedUser, extraClaims);

            return new AuthResponse(token, savedUser.getEmail(),
                    savedUser.getFullName(), savedUser.getRole());

        } catch (Exception e) {
            return new AuthResponse("Registration failed: " + e.getMessage());
        }
    }

    public AuthResponse login(LoginRequest request) {
        try {
            // Authenticate user
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            User user = (User) authentication.getPrincipal();

            // Generate JWT token
            Map<String, Object> extraClaims = new HashMap<>();
            extraClaims.put("role", user.getRole().name());
            extraClaims.put("userId", user.getId());

            String token = jwtUtil.generateToken(user, extraClaims);

            return new AuthResponse(token, user.getEmail(),
                    user.getFullName(), user.getRole());

        } catch (AuthenticationException e) {
            return new AuthResponse("Invalid email or password!");
        } catch (Exception e) {
            return new AuthResponse("Login failed: " + e.getMessage());
        }
    }

    public User getCurrentUser(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }
}

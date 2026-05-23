package com.nguyendinhphuoccao.ecommerce.service.impl;


import com.nguyendinhphuoccao.ecommerce.dto.AuthResponse;
import com.nguyendinhphuoccao.ecommerce.dto.LoginRequest;
import com.nguyendinhphuoccao.ecommerce.dto.RegisterRequest;
import com.nguyendinhphuoccao.ecommerce.model.AuthProvider;
import com.nguyendinhphuoccao.ecommerce.model.User;
import com.nguyendinhphuoccao.ecommerce.repository.UserRepository;
import com.nguyendinhphuoccao.ecommerce.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.nguyendinhphuoccao.ecommerce.security.JwtTokenProvider;
import com.nguyendinhphuoccao.ecommerce.dto.AuthResponse;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider; // Utility class tạo JWT Token

    @Override
    public AuthResponse registerLocal(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email is already taken!");
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .provider(AuthProvider.LOCAL)
                .build();

        userRepository.save(user);
        String accessToken = jwtTokenProvider.generateToken(user.getEmail());
        return new AuthResponse(accessToken, "User registered successfully");
    }

    @Override
    public AuthResponse loginLocal(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        String accessToken = jwtTokenProvider.generateToken(request.getEmail());
        return new AuthResponse(accessToken, "Login successful");
    }

    @Override
    public AuthResponse authenticateOAuth2(String email, String name, String providerStr, String providerId) {
        AuthProvider provider = AuthProvider.valueOf(providerStr.toUpperCase());
        
        User user = userRepository.findByEmail(email).orElseGet(() -> {
            // Nếu chưa có tài khoản thì tự động Đăng ký qua OAuth2
            User newUser = User.builder()
                    .email(email)
                    .name(name)
                    .provider(provider)
                    .providerId(providerId)
                    .build();
            return userRepository.save(newUser);
        });

        String accessToken = jwtTokenProvider.generateToken(user.getEmail());
        return new AuthResponse(accessToken, "OAuth2 Authentication successful");
    }

    @Override
    public void forgotPassword(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
        
        // Logic tạo Reset Token và gửi qua Email (Sử dụng JavaMailSender)
        // ...
        System.out.println("Reset password link sent to: " + email);
    }
}

package com.nguyendinhphuoccao.ecommerce.controller;


import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nguyendinhphuoccao.ecommerce.dto.AuthResponse;
import com.nguyendinhphuoccao.ecommerce.dto.LoginRequest;
import com.nguyendinhphuoccao.ecommerce.dto.RegisterRequest;
import com.nguyendinhphuoccao.ecommerce.service.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // 1. Đăng ký Local
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.registerLocal(request));
    }

    // 2. Đăng nhập Local
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.loginLocal(request));
    }

    // 3. Đăng ký / Đăng nhập bằng Google hoặc Facebook (Client sẽ truyền thông tin từ App Flutter xuống)
    // Thực tế với OAuth2 chuẩn, đoạn này có thể xử lý qua Filter của Spring Security OAuth2 Client.
    @PostMapping("/oauth2/{provider}")
    public ResponseEntity<AuthResponse> oauth2Login(
            @PathVariable String provider,
            @RequestBody Map<String, String> payload) {
        
        String email = payload.get("email");
        String name = payload.get("name");
        String providerId = payload.get("providerId"); // ID từ Google hoặc Facebook
        
        return ResponseEntity.ok(authService.authenticateOAuth2(email, name, provider, providerId));
    }

    // 4. Quên mật khẩu
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestParam String email) {
        authService.forgotPassword(email);
        return ResponseEntity.ok("If the email exists, a reset link will be sent.");
    }
}

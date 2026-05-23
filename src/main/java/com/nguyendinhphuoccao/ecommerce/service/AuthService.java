package com.nguyendinhphuoccao.ecommerce.service;


import com.nguyendinhphuoccao.ecommerce.dto.AuthResponse;
import com.nguyendinhphuoccao.ecommerce.dto.LoginRequest;
import com.nguyendinhphuoccao.ecommerce.dto.RegisterRequest;

public interface AuthService {
    // Đăng ký/Đăng nhập Email + Mật khẩu
    AuthResponse registerLocal(RegisterRequest request);
    AuthResponse loginLocal(LoginRequest request);
    
    // Đăng ký/Đăng nhập OAuth2 (Google/Facebook)
    AuthResponse authenticateOAuth2(String email, String name, String provider, String providerId);
    
    // Quên mật khẩu
    void forgotPassword(String email);
}

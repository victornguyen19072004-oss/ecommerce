package com.nguyendinhphuoccao.ecommerce.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    // Password có thể null nếu người dùng đăng nhập bằng OAuth2 (Google/Facebook)
    private String password; 

    private String name;

    @Enumerated(EnumType.STRING)
    private AuthProvider provider; // ENUM: LOCAL, GOOGLE, FACEBOOK

    private String providerId;
}

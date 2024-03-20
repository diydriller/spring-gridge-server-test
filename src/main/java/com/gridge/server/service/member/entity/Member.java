package com.gridge.server.service.member.entity;

import com.gridge.server.service.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(name = "phone_number")
    private String phoneNumber;
    private String nickname;
    private String password;
    private LocalDate birthday;
    @Enumerated(EnumType.STRING)
    private MemberState state;
    @Column(name = "image_url")
    private String imageUrl;
    @Enumerated(EnumType.STRING)
    private MemberType type;
    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;
    public void updateLastLoginAt() {
        lastLoginAt = LocalDateTime.now();
    }
}

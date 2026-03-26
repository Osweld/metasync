package com.osweld.metasync.identityaccess.internal.infrastructure.persistence.user;

import java.time.LocalDateTime;
import java.util.UUID;

import com.osweld.metasync.identityaccess.internal.domain.model.user.StatusType;
import com.osweld.metasync.identityaccess.internal.domain.model.user.Role;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserJpaEntity {

    @Id
    @Column(name = "user_id", nullable = false, updatable = false)
    private UUID userId;
    @Column(name = "tenant_id", nullable = false, updatable = false)
    private UUID tenantId;
    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;
    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;
    @Column(name = "password_hash", nullable = false, length = 255)
    private String encryptedPassword;
    @Column(name = "email", nullable = false, length = 100)
    private String email;
@Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private StatusType status;
    @Column(name = "role", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private Role role;
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    @Version
    @Column(name = "version", nullable = false)
    private Integer version;

}

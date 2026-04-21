package com.osweld.metasync.identityaccess.infrastructure.persistence.token.account;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

import com.osweld.metasync.identityaccess.domain.model.token.account.TokenStatus;
import com.osweld.metasync.identityaccess.domain.model.token.account.TokenType;

@Entity
@Table(name = "account_tokens", schema = "public")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountTokenJpaEntity {

    @Id
    @Column(name = "account_token_id", nullable = false, updatable = false)
    private UUID accountTokenId;
    @Column(name="user_id", nullable = false, updatable = false)
    private UUID userId;
    @Column(name = "tenant_id", nullable = false, updatable = false)
    private UUID tenantId;
    @Column(name = "token_hash", nullable = false, length = 64)
    private String tokenHash;
    @Enumerated(EnumType.STRING)
    @Column(name="token_type", nullable = false, length = 50)
    private TokenType tokenType;
    @Enumerated(EnumType.STRING)
    @Column(name="token_status", nullable = false, length = 50)
    private TokenStatus tokenStatus;
    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    @Column(name = "used_at")
    private LocalDateTime usedAt;
    @Version
    @Column(name = "version", nullable = false)
    private Integer version;
}

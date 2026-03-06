package com.osweld.metasync.identityaccess.internal.domain.model.token.account;

import java.time.LocalDateTime;
import java.util.Objects;

import com.osweld.metasync.identityaccess.internal.domain.model.AggregateRoot;
import com.osweld.metasync.identityaccess.internal.domain.model.tenant.TenantId;
import com.osweld.metasync.identityaccess.internal.domain.model.user.UserId;

import lombok.Getter;

@Getter
public class AccountToken extends AggregateRoot {

    private final TokenId tokenId;
    private final UserId userId;
    private final TenantId tenantId;
    private final TokenHash tokenHash;
    private final AccountTokenType tokenType;
    private final LocalDateTime expiresAt;
    private final LocalDateTime createdAt;
    private LocalDateTime usedAt;

    private AccountToken(
            TokenId tokenId,
            UserId userId,
            TenantId tenantId,
            TokenHash tokenHash,
            AccountTokenType tokenType,
            LocalDateTime expiresAt,
            LocalDateTime createdAt) {

        if (expiresAt.isBefore(createdAt)) {
            throw new IllegalArgumentException("expiresAt must be after createdAt");
        }

        this.tokenId = Objects.requireNonNull(tokenId, "tokenId must not be null");
        this.userId = Objects.requireNonNull(userId, "userId must not be null");
        this.tenantId = Objects.requireNonNull(tenantId, "tenantId must not be null");
        this.tokenHash = Objects.requireNonNull(tokenHash, "tokenHash must not be null");
        this.tokenType = Objects.requireNonNull(tokenType, "tokenType must not be null");
        this.expiresAt = Objects.requireNonNull(expiresAt, "expiresAt must not be null");
        this.createdAt = Objects.requireNonNull(createdAt, "createdAt must not be null");
    }

    public static AccountToken tenantActivationToken(
            TokenId tokenId,
            UserId userId,
            TenantId tenantId,
            TokenHash tokenHash,
            LocalDateTime expiresAt,
            LocalDateTime createdAt) {

        AccountTokenType tokenType = new AccountTokenType(TokenType.TENANT_ACTIVATION);

        return new AccountToken(tokenId, userId, tenantId, tokenHash, tokenType, expiresAt, createdAt);
    }

    public void consume(LocalDateTime usedAt) {

        if (this.isUsed()) {
            throw new IllegalStateException("Token has already been used");
        }
        if (this.isExpired(usedAt)) {
            throw new IllegalStateException("Token has expired");
        }
        this.usedAt = usedAt;
    }

    public boolean isUsed() {
        return this.usedAt != null;
    }

    public boolean isExpired(LocalDateTime now) {
        Objects.requireNonNull(now, "now must not be null");
        return now.isAfter(this.expiresAt);
    }

}

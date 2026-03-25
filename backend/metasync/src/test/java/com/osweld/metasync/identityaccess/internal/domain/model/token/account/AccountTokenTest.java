package com.osweld.metasync.identityaccess.internal.domain.model.token.account;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

import com.osweld.metasync.identityaccess.internal.domain.model.tenant.TenantId;
import com.osweld.metasync.identityaccess.internal.domain.model.user.UserId;

public class AccountTokenTest {

    private final TokenId tokenId = TokenId.generate();
    private final UserId userId = UserId.generate();
    private final TenantId tenantId = TenantId.generate();
    private final TokenHash tokenHash = new TokenHash("a3a9e1ed9732cab28868127be00f1ce921acaefdd5c3b23a6e9e0072bd9c1a34");
    private final LocalDateTime createdAt = LocalDateTime.now();
    private final LocalDateTime expiresAt = createdAt.plusDays(1);
    private LocalDateTime usedAt = null;


    @Test
    @DisplayName("Should create a valid tenant activation token")
    void shouldCreateValidTenantActivationToken() {
        AccountToken token = AccountToken.tenantActivationToken(
                tokenId,
                userId,
                tenantId,
                tokenHash,
                expiresAt,
                createdAt);

        assertThat(token).isNotNull();
        assertThat(token.getTokenId()).isEqualTo(tokenId);
        assertThat(token.getUserId()).isEqualTo(userId);
        assertThat(token.getTenantId()).isEqualTo(tenantId);
        assertThat(token.getTokenHash()).isEqualTo(tokenHash);
        assertThat(token.getTokenType().value()).isEqualTo(TokenType.TENANT_ACTIVATION);
        assertThat(token.getExpiresAt()).isEqualTo(expiresAt);
        assertThat(token.getCreatedAt()).isEqualTo(createdAt);
        assertThat(token.getUsedAt()).isNull();
        }

    
}

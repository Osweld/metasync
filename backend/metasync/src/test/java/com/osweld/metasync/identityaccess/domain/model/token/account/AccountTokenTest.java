package com.osweld.metasync.identityaccess.domain.model.token.account;

import java.time.Instant;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.osweld.metasync.shared.domain.model.vo.TenantId;

import com.osweld.metasync.identityaccess.domain.model.user.UserId;

import static org.assertj.core.api.Assertions.assertThat;

public class AccountTokenTest {

    private final TokenId tokenId = TokenId.generate();
    private final UserId userId = UserId.generate();
    private final TenantId tenantId = TenantId.generate();
    private final TokenHash tokenHash = new TokenHash("a3a9e1ed9732cab28868127be00f1ce921acaefdd5c3b23a6e9e0072bd9c1a34");
    private final AccountTokenStatus accountTokenStatus = new AccountTokenStatus(TokenStatus.UNUSED);
    private final Instant createdAt = Instant.now();
    private final Instant expiresAt = createdAt.plusSeconds(86_400L);


    AccountToken token = null;

    @BeforeEach
    void setUp() {

        token = AccountToken.tenantActivationToken(
                tokenId,
                userId,
                tenantId,
                tokenHash,
                accountTokenStatus,
                createdAt,
                expiresAt,
                null);
    }


    @Test
    @DisplayName("Should create a valid tenant activation token")
    void shouldCreateValidTenantActivationToken() {

        assertThat(token).isNotNull();
        assertThat(token.getTokenId()).isEqualTo(tokenId);
        assertThat(token.getUserId()).isEqualTo(userId);
        assertThat(token.getTenantId()).isEqualTo(tenantId);
        assertThat(token.getTokenHash()).isEqualTo(tokenHash);
        assertThat(token.getTokenType().value()).isEqualTo(TokenType.TENANT_ACTIVATION);
        assertThat(token.getAccountTokenStatus().value()).isEqualTo(TokenStatus.UNUSED);
        assertThat(token.getExpiresAt()).isEqualTo(expiresAt);
        assertThat(token.getCreatedAt()).isEqualTo(createdAt);
        assertThat(token.getUsedAt()).isNull();
    }

    @Test()
    @DisplayName("Should isUsed return false for unused token")
    void shouldIsUsedReturnFalseForUnusedToken() {

        assertThat(token.getAccountTokenStatus().value()).isEqualTo(TokenStatus.UNUSED);
        assertThat(token.isUsed()).isFalse();

    }

    @Test
    @DisplayName("Should consume token and set usedAt")
    void shouldConsumeTokenAndSetUsedAt() {

        token.markAsUsed(Instant.now());

        assertThat(token.isUsed()).isTrue();
        assertThat(token.getUsedAt()).isNotNull();
        assertThat(token.getAccountTokenStatus().value()).isEqualTo(TokenStatus.USED);

    }

    @Test
    @DisplayName("Should isExpired return false for setting usedAt")
    void shouldIsExpiredReturnFalseForSettingUsedAt() {
        assertThat(token.isExpired(Instant.now())).isFalse();

    }

}

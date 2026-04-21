package com.osweld.metasync.identityaccess.internal.application.port.out;

import java.time.LocalDateTime;
import java.util.Optional;

import com.osweld.metasync.identityaccess.internal.domain.model.token.account.AccountToken;
import com.osweld.metasync.identityaccess.internal.domain.model.token.account.TokenId;

public interface AccountTokenRepository {

    TokenId nextIdentity();
    void saveTenantActivationToken(AccountToken accountToken);
    Optional<AccountToken> findById(TokenId tokenId);
    void accountTokenUsed(TokenId tokenId, LocalDateTime usedAt);
    void accountTokenInvalidated(TokenId tokenId);
    void accountTokenExpired(TokenId tokenId);
}

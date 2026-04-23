package com.osweld.metasync.identityaccess.application.port.out;

import java.time.Instant;
import java.util.Optional;

import com.osweld.metasync.identityaccess.domain.model.token.account.AccountToken;
import com.osweld.metasync.identityaccess.domain.model.token.account.TokenId;

public interface AccountTokenRepository {

    TokenId nextIdentity();
    void saveTenantActivationToken(AccountToken accountToken);
    Optional<AccountToken> findById(TokenId tokenId);
    void accountTokenUsed(TokenId tokenId, Instant usedAt);
    void accountTokenInvalidated(TokenId tokenId);
    void accountTokenExpired(TokenId tokenId);
}

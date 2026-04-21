package com.osweld.metasync.identityaccess.infrastructure.persistence.token.account;

import com.osweld.metasync.identityaccess.application.port.out.AccountTokenRepository;
import com.osweld.metasync.identityaccess.domain.model.token.account.AccountToken;
import com.osweld.metasync.identityaccess.domain.model.token.account.TokenId;

import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.function.Consumer;

@Repository
public class HibernateAccountTokenRepository implements AccountTokenRepository {

    private final AccountTokenJpaRepository accountTokenJpaRepository;
    private final AccountTokenMapper accountTokenMapper;

    public HibernateAccountTokenRepository(AccountTokenJpaRepository accountTokenJpaRepository, AccountTokenMapper accountTokenMapper) {
        this.accountTokenJpaRepository = accountTokenJpaRepository;
        this.accountTokenMapper = accountTokenMapper;
    }

    @Override
    public TokenId nextIdentity() {
        return TokenId.generate();
    }

    @Override
    public void saveTenantActivationToken(AccountToken accountToken) {
        AccountTokenJpaEntity entity = new AccountTokenJpaEntity();
        accountTokenMapper.updateJpaEntity(entity, accountToken);
        accountTokenJpaRepository.save(entity);
    }

    @Override
    public Optional<AccountToken> findById(TokenId tokenId) {
        return accountTokenJpaRepository.findById(tokenId.value())
                .map(accountTokenMapper::toDomainModel);
    }

    @Override
    public void accountTokenUsed(TokenId tokenId, LocalDateTime usedAt) {
        mutateToken(tokenId, token -> token.markAsUsed(usedAt));
    }

    @Override
    public void accountTokenInvalidated(TokenId tokenId) {
        mutateToken(tokenId, AccountToken::markAsInvalid);
    }

    @Override
    public void accountTokenExpired(TokenId tokenId) {
      mutateToken(tokenId, AccountToken::markAsExpired);
    }


    private void mutateToken(TokenId tokenId, Consumer<AccountToken> mutation){
        AccountTokenJpaEntity entity = accountTokenJpaRepository.findById(tokenId.value())
                .orElseThrow(() -> new RuntimeException("Account token not found with id: " + tokenId.value()));

        AccountToken accountToken = accountTokenMapper.toDomainModel(entity);
        mutation.accept(accountToken);
        accountTokenMapper.updateJpaEntity(entity, accountToken);
        accountTokenJpaRepository.save(entity);
    }
}

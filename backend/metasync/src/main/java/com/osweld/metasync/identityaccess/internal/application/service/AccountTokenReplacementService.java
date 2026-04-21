package com.osweld.metasync.identityaccess.internal.application.service;

import com.osweld.metasync.identityaccess.internal.application.port.out.AccountTokenRepository;
import org.springframework.stereotype.Service;

@Service
public class AccountTokenReplacementService {

    private final AccountTokenRepository accountTokenRepository;

    public AccountTokenReplacementService(AccountTokenRepository accountTokenRepository) {
        this.accountTokenRepository = accountTokenRepository;
    }

}

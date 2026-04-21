package com.osweld.metasync.identityaccess.application.service;

import org.springframework.stereotype.Service;

import com.osweld.metasync.identityaccess.application.port.out.AccountTokenRepository;

@Service
public class AccountTokenReplacementService {

    private final AccountTokenRepository accountTokenRepository;

    public AccountTokenReplacementService(AccountTokenRepository accountTokenRepository) {
        this.accountTokenRepository = accountTokenRepository;
    }

}

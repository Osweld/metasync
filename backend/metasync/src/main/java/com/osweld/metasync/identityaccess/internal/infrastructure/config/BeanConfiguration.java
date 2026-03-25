package com.osweld.metasync.identityaccess.internal.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.osweld.metasync.identityaccess.internal.domain.service.EncryptionService;
import com.osweld.metasync.identityaccess.internal.infrastructure.security.FakeEncryptionService;

@Configuration
public class BeanConfiguration {

    @Bean
    public EncryptionService encryptionService() {
        return new FakeEncryptionService();
    }

}

package com.osweld.metasync.identityaccess.internal.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.osweld.metasync.identityaccess.internal.application.port.out.TenantRepository;
import com.osweld.metasync.identityaccess.internal.application.port.out.UserRepository;
import com.osweld.metasync.identityaccess.internal.domain.service.EncryptionService;
import com.osweld.metasync.identityaccess.internal.domain.service.TenantCreator;
import com.osweld.metasync.identityaccess.internal.infrastructure.security.FakeEncryptionService;

@Configuration
public class BeanConfiguration {

    @Bean
    public EncryptionService encryptionService() {
        return new FakeEncryptionService();
    }

    @Bean
    public TenantCreator tenantCreator(EncryptionService encryptionService, TenantRepository tenantRepository,UserRepository userRepository) {
        return new TenantCreator(encryptionService, tenantRepository, userRepository);
    }

}

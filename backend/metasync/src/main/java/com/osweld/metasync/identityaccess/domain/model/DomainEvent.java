package com.osweld.metasync.identityaccess.domain.model;

import java.time.Instant;

public interface DomainEvent {

    Instant occurredOn();

}

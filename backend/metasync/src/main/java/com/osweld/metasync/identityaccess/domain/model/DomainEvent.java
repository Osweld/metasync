package com.osweld.metasync.identityaccess.internal.domain.model;

import java.time.Instant;

public interface DomainEvent {

    Instant occurredOn();

}

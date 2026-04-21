package com.osweld.metasync.identityaccess.domain.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class AggregateRoot {

    private final List<DomainEvent> domainEvents = new ArrayList<>();

    protected void registerDomainEvent(DomainEvent event){
        domainEvents.add(event);
    }


    public List<DomainEvent> pullDomainEvents(){
        List<DomainEvent> events = new ArrayList<>(this.domainEvents);
        domainEvents.clear();
        return Collections.unmodifiableList(events);
    }

}

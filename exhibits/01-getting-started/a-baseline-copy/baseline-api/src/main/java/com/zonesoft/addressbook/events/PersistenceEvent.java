package com.zonesoft.addressbook.events;

import org.springframework.context.ApplicationEvent;

public class PersistenceEvent extends ApplicationEvent {

	private static final long serialVersionUID = 7121033441429890681L;

	public PersistenceEvent(PersistenceEventData source) {		
        super(source);
    }
}

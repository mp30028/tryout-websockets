package com.zonesoft.sayings.api.events;

import org.springframework.context.ApplicationEvent;

import com.zonesoft.sayings.api.entities.TriggerMessage;

public class TriggerEvent  extends ApplicationEvent {

	private static final long serialVersionUID = 6559632374596973782L;

	public TriggerEvent(TriggerMessage message) {
		super(message);
	}
	
}

package com.zonesoft.ticker.api.events;

import org.springframework.context.ApplicationEvent;

import com.zonesoft.ticker.api.entities.TickMessage;

public class TickEvent  extends ApplicationEvent {

	private static final long serialVersionUID = 6559632374596973782L;

	public TickEvent(TickMessage message) {
		super(message);
	}
	
}

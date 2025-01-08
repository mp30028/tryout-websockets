package com.zonesoft.sayings.api.events;

import org.springframework.context.ApplicationEvent;

import com.zonesoft.sayings.api.entities.MessageEventContent;

public class MessageEvent  extends ApplicationEvent {

	private static final long serialVersionUID = 6559632374596973782L;

	public MessageEvent(MessageEventContent message) {
		super(message);
	}
	
}

package com.zonesoft.person.api.events;

import org.springframework.context.ApplicationEvent;

public class DbEvent<T>  extends ApplicationEvent {

	private static final long serialVersionUID = -3182869821041185432L;

	public DbEvent(DbEventContent<T> content) {
		super(content);
	}
	
}

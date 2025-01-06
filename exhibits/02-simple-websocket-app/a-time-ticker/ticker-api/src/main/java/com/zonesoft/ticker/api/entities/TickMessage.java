package com.zonesoft.ticker.api.entities;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import com.zonesoft.ticker.api.utils.ToStringHelper;


public class TickMessage {
	private ZonedDateTime sentAt;
	private String sender;
	private String message;
	
	public TickMessage(String sender, String message) {
		super();
		this.sentAt = ZonedDateTime.now(ZoneId.of("UTC"));
		this.sender = sender;
		this.message = message;		
	}

	public ZonedDateTime getSentAt() {
		return sentAt;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	@Override
	public String toString() {
		ToStringHelper helper = new ToStringHelper();
		return helper.begin()
			.wrLn("sender", this.sender)
			.wrLn("message", this.message)
			.wr("sentAt", this.sentAt)
		.end().build();		
	}	
	
}

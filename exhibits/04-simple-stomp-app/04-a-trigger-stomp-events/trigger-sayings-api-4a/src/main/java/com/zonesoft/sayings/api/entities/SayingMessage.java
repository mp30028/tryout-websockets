package com.zonesoft.sayings.api.entities;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import com.zonesoft.sayings.api.utils.ToStringHelper;


public class SayingMessage {
	private ZonedDateTime sentAt;
	private String sender;
	private Saying saying;
	
	public SayingMessage(String sender, Saying saying) {
		super();
		this.sentAt = ZonedDateTime.now(ZoneId.of("UTC"));
		this.sender = sender;
		this.saying = saying;		
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

	public Saying getSaying() {
		return saying;
	}

	public void setSaying(Saying saying) {
		this.saying = saying;
	}
	
	@Override
	public String toString() {
		ToStringHelper helper = new ToStringHelper();
		return helper.begin()
			.wrLn("sender", this.sender)
			.wrLn("saying", this.saying)
			.wr("sentAt", this.sentAt)
		.end().build();		
	}	
	
}

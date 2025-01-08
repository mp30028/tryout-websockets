package com.zonesoft.sayings.api.entities;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import com.zonesoft.sayings.api.utils.ToStringHelper;


public class MessageEventContent {
	private ZonedDateTime sentAt;
	private String sender;
	private String content;
	
	public MessageEventContent(String sender, String content) {
		super();
		this.sentAt = ZonedDateTime.now(ZoneId.of("UTC"));
		this.sender = sender;
		this.content = content;		
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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	@Override
	public String toString() {
		ToStringHelper helper = new ToStringHelper();
		return helper.begin()
			.wrLn("sender", this.sender)
			.wrLn("content", this.content)
			.wr("sentAt", this.sentAt)
		.end().build();		
	}	
	
}

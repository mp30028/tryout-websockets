package com.zonesoft.sayings.api.entities;

import com.zonesoft.sayings.api.utils.ToStringHelper;

public class Saying {
	private String content;

	public Saying() {
	}

	public Saying(String content) {
		this.content = content;
	}

	public String getContent() {
		return content;
	}
	
	@Override
	public String toString() {
		ToStringHelper helper = new ToStringHelper();
		return helper.begin()
			.wr("content", this.content)
		.end().build();		
	}	
	
}

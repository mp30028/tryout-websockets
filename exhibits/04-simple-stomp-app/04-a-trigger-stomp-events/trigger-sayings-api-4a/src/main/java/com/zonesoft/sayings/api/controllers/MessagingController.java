package com.zonesoft.sayings.api.controllers;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zonesoft.sayings.api.entities.Saying;
import com.zonesoft.sayings.api.entities.SayingMessage;
import com.zonesoft.sayings.api.utils.Sayings;


@Controller
public class MessagingController {
	private static final Logger LOGGER = LoggerFactory.getLogger(MessagingController.class);
	private static final Sayings sayings = new Sayings();
	
	@MessageMapping("/trigger-saying")
	@SendTo("/topic/sayings")	
	public SayingMessage saying(String senderJson) throws Exception {
		Saying saying = new Saying( String.join(": ", getSaying()));
		SayingMessage tmsg = new SayingMessage(getSenderFromJson(senderJson), saying);
		return tmsg;
	}

	private String getSenderFromJson(String json) throws JsonMappingException, JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		@SuppressWarnings("unchecked")
		Map<String,Object> map = mapper.readValue(json, Map.class);
		LOGGER.debug("json in request-body = {} mapped as {}",json, map);
		return (map.containsKey("sender")) ? map.get("sender").toString() : "<sender-unavailable>"; 		 
	}	
	
	private String[] getSaying(){
		int startOfIndex = 0;
		int endOfIndex = sayings.SAYINGS.length -1;
		int randomNumberSelected = (int) ((Math.random() * (endOfIndex - startOfIndex)) + startOfIndex);
		return sayings.SAYINGS[randomNumberSelected];
	}	
}

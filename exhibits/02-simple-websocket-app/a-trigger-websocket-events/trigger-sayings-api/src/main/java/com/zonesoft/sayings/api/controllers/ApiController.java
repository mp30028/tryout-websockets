package com.zonesoft.sayings.api.controllers;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zonesoft.sayings.api.entities.MessageEventContent;
import com.zonesoft.sayings.api.events.MessageEvent;
import com.zonesoft.sayings.api.utils.Sayings;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/sayings")
public class ApiController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ApiController.class);	
	private static final Sayings sayings = new Sayings();
	private ApplicationEventPublisher  publisher;
	
	public ApiController(ApplicationEventPublisher  publisher) {
		this.publisher = publisher;
	}
		
	@GetMapping
	public Mono<ResponseEntity<MessageEventContent>> getMessage(){
		String sender = "Dummy";
		String message = String.join(": ", getSaying());  
		MessageEventContent msg = new MessageEventContent(sender, message);
		return Mono.just(ResponseEntity.ok().body(msg));
	}
	
	@PutMapping
	public Mono<ResponseEntity<MessageEvent>> messageEvent(@RequestBody String json) throws JsonMappingException, JsonProcessingException{		
		String message = String.join(": ", getSaying());
		MessageEventContent tmsg = new MessageEventContent(getSenderFromJson(json), message);
		MessageEvent event = new MessageEvent(tmsg);		
		this.publisher.publishEvent(event);	
		LOGGER.debug("Event published {}", event);
		return Mono.just(ResponseEntity.ok().body(event));		
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

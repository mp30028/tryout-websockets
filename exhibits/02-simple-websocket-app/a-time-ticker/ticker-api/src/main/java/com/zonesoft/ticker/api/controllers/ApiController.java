package com.zonesoft.ticker.api.controllers;

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
import com.zonesoft.ticker.api.entities.TickMessage;
import com.zonesoft.ticker.api.events.TickEvent;
import com.zonesoft.ticker.api.utils.Sayings;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/ticker")
public class ApiController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ApiController.class);	
	private static final Sayings sayings = new Sayings();
	private ApplicationEventPublisher  publisher;
	
	public ApiController(ApplicationEventPublisher  publisher) {
		this.publisher = publisher;
	}
		
	@GetMapping
	public Mono<ResponseEntity<TickMessage>> getMessage(){
		String sender = "Dummy";
		String message = String.join(": ", getSaying());  
		TickMessage msg = new TickMessage(sender, message);
		return Mono.just(ResponseEntity.ok().body(msg));
	}
	
	@PutMapping
	public Mono<ResponseEntity<TickEvent>> triggerEvent(@RequestBody String json) throws JsonMappingException, JsonProcessingException{		
		String message = String.join(": ", getSaying());
		TickMessage tmsg = new TickMessage(getSenderFromJson(json), message);
		TickEvent event = new TickEvent(tmsg);		
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

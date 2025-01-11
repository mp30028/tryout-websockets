package com.zonesoft.person.api.controllers;

import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ChangeStreamOptions;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.data.mongodb.core.ChangeStreamOptions.ChangeStreamOptionsBuilder;
import org.springframework.stereotype.Controller;

import com.mongodb.client.model.changestream.FullDocument;
import com.zonesoft.person.api.events.DbEvent;
import com.zonesoft.person.api.events.DbEventContent;

@Controller
public class MessageController<T>{
	private static final Logger LOGGER = LoggerFactory.getLogger(MessageController.class);
	private final ReactiveMongoTemplate mongoTemplate;
	private final SimpMessagingTemplate messagingTemplate;
	
    @Autowired
	public MessageController(ReactiveMongoTemplate mongoTemplate, SimpMessagingTemplate messagingTemplate) {
		this.mongoTemplate = mongoTemplate;
		this.messagingTemplate = messagingTemplate;
		publishMongoEvents();
	}
           
	@SuppressWarnings("unchecked")
	private void publishMongoEvents() {	
		
	    Consumer<ChangeStreamOptionsBuilder> optionsBuilderConsumer = ( (b) -> 
	    	ChangeStreamOptions.builder()
            .returnFullDocumentOnUpdate()
            .returnFullDocumentBeforeChange()
            .fullDocumentLookup(FullDocument.WHEN_AVAILABLE)
        );
	    	    
		mongoTemplate
				.changeStream((Class<T>) Object.class)
				.withOptions(optionsBuilderConsumer)
				.watchCollection("persons")
				.listen()				
				.map(e -> new DbEventContent<T>(e.getRaw(), (T) e.getBody() ))
				.map(content -> new DbEvent<T>(content))
				.subscribe(event -> {
					LOGGER.debug("converting and publishing to \"/topic/db-event\" the db-event {}", event);
					messagingTemplate.convertAndSend("/topic/db-event", event);
				});
	}
    
}

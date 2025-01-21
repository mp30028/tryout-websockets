package com.zonesoft.person.api.controllers;

import java.util.List;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ChangeStreamOptions;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.data.mongodb.core.ChangeStreamOptions.ChangeStreamOptionsBuilder;
import org.springframework.stereotype.Controller;

import com.mongodb.client.model.changestream.FullDocument;
import com.zonesoft.person.api.entities.Person;
import com.zonesoft.person.api.events.DbEvent;
import com.zonesoft.person.api.events.DbEventContent;
import com.zonesoft.person.api.services.PersonService;

@Controller
public class MessageController{
	private static final Logger LOGGER = LoggerFactory.getLogger(MessageController.class);
	private final ReactiveMongoTemplate mongoTemplate;
	private final SimpMessagingTemplate messagingTemplate;
	private final PersonService service;
	
    @Autowired
	public MessageController(ReactiveMongoTemplate mongoTemplate, SimpMessagingTemplate messagingTemplate, PersonService service) {
    	super();
		this.mongoTemplate = mongoTemplate;
		this.messagingTemplate = messagingTemplate;
		this.service = service;
		publishMongoEvents();
		
	}
    
	private void publishMongoEvents() {	
		
	    Consumer<ChangeStreamOptionsBuilder> optionsBuilderConsumer = ( (b) -> 
	    	ChangeStreamOptions.builder()
            .returnFullDocumentOnUpdate()
            .returnFullDocumentBeforeChange()
            .fullDocumentLookup(FullDocument.WHEN_AVAILABLE)
        );
	    	    
		mongoTemplate
				.changeStream(Person.class)
				.withOptions(optionsBuilderConsumer)
				.watchCollection("persons")
				.listen()				
				.map(e -> new DbEventContent<Person>(e.getRaw(), e.getBody() ))
				.map(content -> new DbEvent<Person>(content))
				.subscribe(event -> {
					LOGGER.debug("converting and publishing to \"/topic/person/events\" the db-event {}", event);					
					messagingTemplate.convertAndSend("/topic/person/events", event);
				});
	};
	
	@MessageMapping("/person/get-all")
	@SendTo("/topic/person/get-all")
    public List<Person> getAll(){
    	return service.findAll().collectList().as((m)->m.block());
    }	
	
	@MessageMapping("/person/get-by-id")
	@SendTo("/topic/person/get-by-id")
    public List<Person> getById(List<String> listOfIds){
		LOGGER.debug("listOfIds={}", listOfIds);
    	return service.findByListOfIds(listOfIds).collectList().as((m)->m.block());
    }		
	
	@MessageMapping("/person/get-by-moniker")
	@SendTo("/topic/person/get-by-moniker")
    public List<Person> getByMoniker(String moniker){
		LOGGER.debug("moniker={}", moniker);
    	return service.findByMoniker(moniker).collectList().as((m)->m.block());
    }
	
	@MessageMapping("/person/insert")
	@SendTo("/topic/person/insert")
    public Person insert( Person person){
    	return service.insert(person).as((m)-> m.block());
    }

	@MessageMapping("/person/update")
	@SendTo("/topic/person/update")
    public Person update( Person person){
    	return service.update(person).as((m)-> m.block());
    }	
	
	@MessageMapping("/person/delete")
	@SendTo("/topic/person/delete")
    public Person delete(Person person){
    	return service.delete(person).thenReturn(person).as((m)-> m.block());
    }

}

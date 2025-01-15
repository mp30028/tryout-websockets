package com.zonesoft.person.api.controllers;

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

//import reactor.core.publisher.Mono;

@Controller
public class MessageController<T>{
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
    
	@MessageMapping("/person/insert")
	@SendTo("/topic/person/insert")
    public Person insert( Person person){
    	return service.insert(person).as((m)-> m.block());        
    }
        
//    @GetMapping
//    public Mono<ResponseEntity<List<Person>>> findAll(){
//    	Flux<Person> personFlux = service.findAll();
//    	if (Objects.nonNull(personFlux)) {
//	        return personFlux
//	        	.collectList()
//	        	.map( l -> {
//	        		if (Objects.nonNull(l) && (l.size()>0)) {
//	        			return ResponseEntity.ok().body(l);	
//	        		}else {
//	        			return ResponseEntity.noContent().build();
//	        		}
//	        	});
//	    }else {
//	    	return Mono.just(ResponseEntity.noContent().build());
//	    }
//    }
//
//    @GetMapping(params = {"id"})
//    public Mono<ResponseEntity<List<Person>>> findByListOfIds(@RequestParam List<String> id){
//    	Flux<Person> personFlux = service.findByListOfIds(id);
//    	if (Objects.nonNull(personFlux)) {
//	        return personFlux
//	        	.collectList()
//	        	.map( l -> {
//	        		if (Objects.nonNull(l) && (l.size()>0)) {
//	        			return ResponseEntity.ok().body(l);	
//	        		}else {
//	        			return ResponseEntity.noContent().build();
//	        		}
//	        	});
//	    }else {
//	    	return Mono.just(ResponseEntity.noContent().build());
//	    }
//    }
//
//    @GetMapping("/{id}")
//    public Mono<ResponseEntity<Person>> findById(@PathVariable String id){
//        Mono<Person> personMono = service.findById(id);
//        return personMono.map(ResponseEntity::ok)
//                .defaultIfEmpty(ResponseEntity.notFound().build());
//    }
//
//    @GetMapping(params = {"moniker"})
//    public Mono<ResponseEntity<List<Person>>> findByMoniker(@RequestParam String moniker){
//    	Flux<Person> personFlux = service.findByMoniker(moniker);
//    	if (Objects.nonNull(personFlux)) {
//	        return personFlux
//	        	.collectList()
//	        	.map( l -> {
//	        		if (Objects.nonNull(l) && (l.size()>0)) {
//	        			return ResponseEntity.ok().body(l);	
//	        		}else {
//	        			return ResponseEntity.noContent().build();
//	        		}
//	        	});
//	    }else {
//	    	return Mono.just(ResponseEntity.noContent().build());
//	    }
//    	
//    }
//    
//    @PutMapping("/{id}")
//    public Mono<ResponseEntity<Person>> update(@PathVariable String id, @RequestBody Person person){
//    	LOGGER.debug("FROM ApiController.update: id={}, person={}", id, person);
//    	person.setId(id);
//        return service.update(person)
//        		.map(updated -> {
//        			LOGGER.debug("FROM ApiController.update: updatedPerson={}", updated);
//        			return updated;
//        		})
//                .map( p -> ResponseEntity.accepted().body(p));
//    }
//
//    @DeleteMapping("/{id}")
//    public Mono<ResponseEntity<Void>> deleteById(@PathVariable String id){
//        return service.deleteById(id)
//                .map( r -> ResponseEntity.accepted().<Void>build());
//    }	
	
}

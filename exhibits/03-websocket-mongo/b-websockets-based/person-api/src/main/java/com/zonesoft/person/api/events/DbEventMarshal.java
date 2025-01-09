package com.zonesoft.person.api.events;

import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ChangeStreamOptions;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.ChangeStreamOptions.ChangeStreamOptionsBuilder;
import org.springframework.stereotype.Component;

import com.mongodb.client.model.changestream.FullDocument;

import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

@Component
public class DbEventMarshal<T> implements Consumer<FluxSink<DbEvent<T>>>{
	private static final Logger LOGGER = LoggerFactory.getLogger(DbEventMarshal.class);
	private final ReactiveMongoTemplate template;
	
    @Autowired
	public DbEventMarshal(ReactiveMongoTemplate template) {
		this.template = template;
	}
    
	@Override
	public void accept(FluxSink<DbEvent<T>> sink){
		LOGGER.debug("From accept");
		publishMongoEvents().map(e -> sink.next(e)).subscribe();		
	}    
    
	@SuppressWarnings("unchecked")
	private Flux<DbEvent<T>> publishMongoEvents() {	
		
	    Consumer<ChangeStreamOptionsBuilder> optionsBuilderConsumer = ( (b) -> 
	    	ChangeStreamOptions.builder()
            .returnFullDocumentOnUpdate()
            .returnFullDocumentBeforeChange()
            .fullDocumentLookup(FullDocument.WHEN_AVAILABLE)
        );
	    	    
		return template
				.changeStream((Class<T>) Object.class)
				.withOptions(optionsBuilderConsumer)
				.watchCollection("persons")
				.listen()				
				.map(e -> new DbEventContent<T>(e.getRaw(), (T) e.getBody() ))
				.map(content -> new DbEvent<T>(content));
	}
}

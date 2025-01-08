package com.zonesoft.person.api.services;

import java.util.List;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ChangeStreamOptions;
import org.springframework.data.mongodb.core.ChangeStreamOptions.ChangeStreamOptionsBuilder;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Service;

import com.mongodb.client.model.changestream.FullDocument;
import com.zonesoft.person.api.entities.DbEvent;
import com.zonesoft.person.api.entities.Person;
import com.zonesoft.person.api.repositories.PersonRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class PersonService {

	private static final Logger LOGGER = LoggerFactory.getLogger(PersonService.class);
	
	private final PersonRepository repository;
	private final ReactiveMongoTemplate template;
	
	@Autowired
	public PersonService(PersonRepository repository, ReactiveMongoTemplate template) {
		this.repository = repository;
		this.template = template;
	}
	
	public Mono<Person> insert(Person person) {
		LOGGER.debug("PersonService.insert: person={}", person);
		return repository.insert(person);
	}
	
	public Mono<Person> update(Person person){
		LOGGER.debug("PersonService.update: person={}", person);
		return repository.save(person);
	}
	
	public Mono<Person> save(Person person){
		 return repository.save(person);
   }
	
	public Flux<Person> saveAll(List<Person> persons){
		 return repository.saveAll(persons);
    }
    
	public Mono<Person> findById(String id){
    	return repository.findById(id);
    }
    
	public Flux<Person> findByListOfIds(List<String> listOfId){
    	return repository.findByIdIn(listOfId);
    }
		
	public Flux<Person> findAll(){
		return repository.findAll();
	}
	
	public Mono<Void> deleteAll(){		
		return repository.deleteAll();
    }
    
	public Mono<Void> deleteById(String id){
		return repository.deleteById(id);
    }
	
	public Flux<Person> findByMoniker(String moniker){
    	return repository.findByMoniker(moniker);
    }
	
	public Flux<DbEvent<Person>> events() {	
		
	    Consumer<ChangeStreamOptionsBuilder> optionsBuilderConsumer = ( (b) -> 
	    	ChangeStreamOptions.builder()
            .returnFullDocumentOnUpdate()
            .returnFullDocumentBeforeChange()
            .fullDocumentLookup(FullDocument.WHEN_AVAILABLE)
        );
	    	    
		Flux<DbEvent<Person>> flux = template
				.changeStream(Person.class)
				.withOptions(optionsBuilderConsumer)
				.watchCollection("persons")
				.listen()				
				.map(e -> new DbEvent<Person>(e.getRaw(), e.getBody() ));
		return flux;	    	    
	}

}

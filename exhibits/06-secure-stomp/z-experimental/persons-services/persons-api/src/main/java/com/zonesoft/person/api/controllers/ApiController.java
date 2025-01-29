package com.zonesoft.person.api.controllers;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zonesoft.person.api.entities.Person;
import com.zonesoft.person.api.services.PersonService;

import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/person")
public class ApiController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApiController.class);
	private final PersonService service;
    
	@Autowired
    public ApiController(PersonService service) {
    	super();
    	this.service = service;
    }

    @PostMapping
    public Mono<ResponseEntity<Person>> insert(@RequestBody Person person){
    	Mono<Person> personMono = service.insert(person);
        return personMono.map(p-> ResponseEntity.created(null).body(p))
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }
        
    @GetMapping
    public Mono<ResponseEntity<List<Person>>> findAll(){
    	Flux<Person> personFlux = service.findAll();
    	if (Objects.nonNull(personFlux)) {
	        return personFlux
	        	.collectList()
	        	.map( l -> {
	        		if (Objects.nonNull(l) && (l.size()>0)) {
	        			return ResponseEntity.ok().body(l);	
	        		}else {
	        			return ResponseEntity.noContent().build();
	        		}
	        	});
	    }else {
	    	return Mono.just(ResponseEntity.noContent().build());
	    }
    }

    @GetMapping(params = {"id"})
    public Mono<ResponseEntity<List<Person>>> findByListOfIds(@RequestParam List<String> id){
    	Flux<Person> personFlux = service.findByListOfIds(id);
    	if (Objects.nonNull(personFlux)) {
	        return personFlux
	        	.collectList()
	        	.map( l -> {
	        		if (Objects.nonNull(l) && (l.size()>0)) {
	        			return ResponseEntity.ok().body(l);	
	        		}else {
	        			return ResponseEntity.noContent().build();
	        		}
	        	});
	    }else {
	    	return Mono.just(ResponseEntity.noContent().build());
	    }
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Person>> findById(@PathVariable String id){
        Mono<Person> personMono = service.findById(id);
        return personMono.map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping(params = {"moniker"})
    public Mono<ResponseEntity<List<Person>>> findByMoniker(@RequestParam String moniker){
    	Flux<Person> personFlux = service.findByMoniker(moniker);
    	if (Objects.nonNull(personFlux)) {
	        return personFlux
	        	.collectList()
	        	.map( l -> {
	        		if (Objects.nonNull(l) && (l.size()>0)) {
	        			return ResponseEntity.ok().body(l);	
	        		}else {
	        			return ResponseEntity.noContent().build();
	        		}
	        	});
	    }else {
	    	return Mono.just(ResponseEntity.noContent().build());
	    }
    	
    }
    
    @PutMapping("/{id}")
    public Mono<ResponseEntity<Person>> update(@PathVariable String id, @RequestBody Person person){
    	LOGGER.debug("FROM ApiController.update: id={}, person={}", id, person);
    	person.setId(id);
        return service.update(person)
        		.map(updated -> {
        			LOGGER.debug("FROM ApiController.update: updatedPerson={}", updated);
        			return updated;
        		})
                .map( p -> ResponseEntity.accepted().body(p));
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteById(@PathVariable String id){
    	LOGGER.debug("FROM ApiController.delete: id={}", id);
        return service.deleteById(id)
                .map( r -> ResponseEntity.accepted().<Void>build());
    }
    
}
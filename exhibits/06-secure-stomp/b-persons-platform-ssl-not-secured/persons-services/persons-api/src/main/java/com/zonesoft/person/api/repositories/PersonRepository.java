package com.zonesoft.person.api.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.zonesoft.person.api.entities.Person;

import reactor.core.publisher.Flux;

public interface PersonRepository extends ReactiveMongoRepository<Person, String>{
	
	public Flux<Person> findByMoniker(String moniker);
	
	public Flux<Person> findByIdIn(List<String> ids);
}

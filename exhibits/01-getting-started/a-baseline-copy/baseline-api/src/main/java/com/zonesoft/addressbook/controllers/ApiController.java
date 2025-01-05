package com.zonesoft.addressbook.controllers;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.zonesoft.addressbook.entities.Person;
import com.zonesoft.addressbook.services.PersonDataService;

@RestController
@RequestMapping("/persons")
public class ApiController {
	private static final Logger LOGGER = LoggerFactory.getLogger(ApiController.class);
	
	private final PersonDataService personDataService;
	
	public ApiController(PersonDataService personDataService) {
		this.personDataService = personDataService;
	}
	
	
	@GetMapping("/get-all")
	@ResponseBody
	public List<Person> getAll(){
		return (List<Person>) personDataService.getAll();
	}
	
	@PostMapping("/add-new")
	@ResponseBody
	public Person addNew(@RequestBody  Person person) {
		LOGGER.debug("Adding new person {}", person.toString());
		return personDataService.addNew(person);
	}
	
	@PostMapping("/update")
	@ResponseBody
	public Person update(@RequestBody  Person person) {
		LOGGER.debug("Updating person {}", person.toString());
		return personDataService.update(person);
	}
	
	@GetMapping("/by-id/{id}")
	@ResponseBody
	public Optional<Person> getById(@PathVariable Long id) {
		Optional<Person> resultOfFind = personDataService.getById(id);
		return resultOfFind;
	}
	
	@DeleteMapping("delete/{id}")
	public void delete(@PathVariable  Long id){
		personDataService.delete(id);
	}
	
}

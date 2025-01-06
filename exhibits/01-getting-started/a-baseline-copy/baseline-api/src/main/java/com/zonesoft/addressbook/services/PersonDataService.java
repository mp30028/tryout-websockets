package com.zonesoft.addressbook.services;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zonesoft.addressbook.entities.OtherName;
import com.zonesoft.addressbook.entities.OtherNameType;
import com.zonesoft.addressbook.entities.Person;
import com.zonesoft.addressbook.exceptions.AddressbookException;
import com.zonesoft.addressbook.repositories.OtherNameTypeRepository;
import com.zonesoft.addressbook.repositories.PersonRepository;

@Service
public class PersonDataService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PersonDataService.class);
	private final PersonRepository personRepository;
	private final OtherNameTypeRepository otherNameTypeRepository;
	
	@Autowired
	public PersonDataService(PersonRepository personRepository, OtherNameTypeRepository otherNameTypeRepository) {
		this.personRepository = personRepository;
		this.otherNameTypeRepository = otherNameTypeRepository;
	}
	
	public List<Person> getAll(){
		return (List<Person>) personRepository.findAll();
	}
	
	public Optional<Person> getById(Long id) {
		Optional<Person> resultOfFind = personRepository.findById(id);
		return resultOfFind;
	}
	
	@Transactional
	public Person addNew(Person person) {
		fixUpPersonsOtherNames(person);
		Person savedPerson = personRepository.save(person);
		return savedPerson;
	}
	
	@Transactional
	public Person update(Person person) {
		Person savedPerson = null;
		if (Objects.nonNull(person)) {
			if (Objects.nonNull(person.getId())) {
				Optional<Person> returned = personRepository.findById(person.getId());
				if (returned.isPresent()) {
					fixUpPersonsOtherNames(person);
					Person returnedPerson = returned.get();
					returnedPerson.setFirstname(person.getFirstname());
					returnedPerson.setLastname(person.getLastname());
					returnedPerson.setDateOfBirth(person.getDateOfBirth());
					returnedPerson.setOtherNames(person.getOtherNames());
					savedPerson = personRepository.save(returnedPerson);
				} else {
					throwAddressBookException(String.format("The provided person has id=%s which could not be found in db. Update is not possible",person.getId()));
				}
			} else {
				throwAddressBookException("The provided person has id set to null. Update is not possible");
			}
		} else {
			throwAddressBookException("The provided person value is null. Update is not possible");
		}
		return savedPerson;
	}

	public void delete(Long id){	
		personRepository.deleteById(id);
	}
	
	private Optional<OtherNameType> getOtherNameTypeByValue(String value) {
		List<OtherNameType> result = otherNameTypeRepository.findByValue(value);
		if (result.size() > 0) {
			return Optional.of(result.get(0));
		}else {
			return Optional.empty();
		}
	}
	
	private void throwAddressBookException(String message) {
		AddressbookException exception = new AddressbookException(message);
		LOGGER.error(message);
		throw new AddressbookException(exception);
	}
	
	
	private void fixUpPersonsOtherNames(Person person) {
		if (Objects.nonNull(person)) {
			if (Objects.nonNull(person.getOtherNames())) {
				for (OtherName otherName : person.getOtherNames()) {
					fixUpOtherNamesTypes(otherName);
					otherName.setPerson(person);
				}
			}
		}
	}

	private void fixUpOtherNamesTypes(OtherName otherName) {
		if (Objects.nonNull(otherName)) {
			OtherNameType otherNameType = otherName.getOtherNameType();
			if (Objects.nonNull(otherNameType)) {
				if (Objects.nonNull(otherNameType.getId())) {
					Optional<OtherNameType> result = otherNameTypeRepository.findById(otherNameType.getId());
					if (result.isEmpty()) {
						String message = String.format("The provided OtherNameType.id = '%s' is invalid",
								otherNameType.getId());
						AddressbookException exception = new AddressbookException(message);
						LOGGER.error(message);
						throw new AddressbookException(exception);
					} else {
						otherNameType = result.get();
						otherName.setOtherNameType(otherNameType);
						LOGGER.debug("otherNameType of otherName verified and set. otherName = {}", otherName);
					}
				} else if (Objects.nonNull(otherNameType.getValue())) {
					Optional<OtherNameType> result = getOtherNameTypeByValue(otherNameType.getValue());
					if (result.isEmpty()) {
						String message = String.format("The provided OtherNameType.value = '%s' is invalid",
								otherNameType.getValue());
						AddressbookException exception = new AddressbookException(message);
						LOGGER.error(message);
						throw new AddressbookException(exception);
					} else {
						otherNameType = result.get();
						otherName.setOtherNameType(otherNameType);
						LOGGER.debug("otherNameType of otherName verified and set. otherName = {}", otherName);
					}
				} else {
					String message = String.format(
							"OtherNameType is required but was not provided. otherName= '%s' is therefore invalid",
							otherName);
					AddressbookException exception = new AddressbookException(message);
					LOGGER.error(message);
					throw new AddressbookException(exception);
				}
			}
		}else {
			LOGGER.warn("otherName is null");
		}
	}
}

package com.zonesoft.addressbook.repositories;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.zonesoft.addressbook.entities.Person;
import com.zonesoft.addressbook.events.PersistenceEvent;
import com.zonesoft.addressbook.events.PersistenceEventData;
import com.zonesoft.addressbook.events.PersistenceEventType;
import com.zonesoft.addressbook.events.forwarders.JpaEventForwarder;
import com.zonesoft.addressbook.events.forwarders.PersistenceEventForwarder;
//import com.zonesoft.addressbook.events.PersistenceEventPublisherConfiguration;
import com.zonesoft.addressbook.repositories.PersonRepository;

import org.testcontainers.ext.ScriptUtils;
import org.testcontainers.jdbc.JdbcDatabaseDelegate;

@Testcontainers()
//@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@ActiveProfiles("test")
//@ContextConfiguration(classes = {PersistenceEventForwarder.class,JpaEventForwarder.class,PersistenceEventData.class,PersistenceEvent.class,PersistenceEventType.class})
@SpringBootTest
class PersonRepositoryDbIntegrationTest extends AbstractMySqlContainer {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PersonRepositoryDbIntegrationTest.class);
	
	@DynamicPropertySource
	static void setProperties(DynamicPropertyRegistry registry) {
	    registry.add("spring.datasource.url", MYSQL_CONTAINER::getJdbcUrl);
	    registry.add("spring.datasource.password", MYSQL_CONTAINER::getPassword);
	    registry.add("spring.datasource.username", MYSQL_CONTAINER::getUsername);  
	}
	
	@Autowired
	private PersonRepository personRepository;
	
	
	@BeforeAll
	static void initialiseTestContainerDb() {
		ScriptUtils.runInitScript(new JdbcDatabaseDelegate(MYSQL_CONTAINER, ""),"create_test_db.sql");
		
	}
	
	@Test
	void testFindAll_GIVEN_initialisedByLoadScript_THEN_ReturnsExpectedNumberOfRecords() {
		assertNotNull(personRepository);
		int EXPECTED_SIZE = 6;
		Iterable<Person> searchResults = personRepository.findAll();
		List<Person> result = StreamSupport.stream(searchResults.spliterator(), false).collect(Collectors.toList());
		assertEquals(EXPECTED_SIZE, result.size());
	}
	
	@Test
	void testFindById_GIVEN_validIds_THEN_isPresentShouldBeTrue() {
		Long[] ids = new Long[] {1L, 2L ,3L ,4L ,5L, 16L};
		for (Long id : ids) {
			 Optional<Person> foundPerson = personRepository.findById(id);
			 assertTrue(foundPerson.isPresent());
			 LOGGER.debug("foundPerson={}", foundPerson.get());
		}
	}

	@Test
	void testFindById_GIVEN_anInvalidId_THEN_isEmptyShouldBeTrue() {
		Long id = 123L;
		 Optional<Person> foundPerson = personRepository.findById(id);
		 assertTrue(foundPerson.isEmpty());
	}
	
	
}
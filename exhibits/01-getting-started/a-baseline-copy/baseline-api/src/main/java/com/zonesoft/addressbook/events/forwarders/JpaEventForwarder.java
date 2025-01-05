package com.zonesoft.addressbook.events.forwarders;

import javax.persistence.PostLoad;
import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.zonesoft.addressbook.entities.Person;
import com.zonesoft.addressbook.events.PersistenceEvent;
import com.zonesoft.addressbook.events.PersistenceEventData;
import com.zonesoft.addressbook.events.PersistenceEventType;

@Service
public class JpaEventForwarder{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(JpaEventForwarder.class);
	private final ApplicationEventPublisher  publisher;
	
	@Autowired
	public JpaEventForwarder(ApplicationEventPublisher  publisher) {
		this.publisher = publisher;
	}
	
    @PostPersist
    public void afterInsert(Person person) {
        LOGGER.info("[JPA-EVENT] CREATE completed for PERSON: {} ", person.toString());
        PersistenceEventData source = new PersistenceEventData(PersistenceEventType.CREATE, person);
        publisher.publishEvent(new PersistenceEvent(source));
    }
    
    @PostLoad
    public void afterRead(Person person) {
        LOGGER.info("[JPA-EVENT] READ completed for PERSON: {} ", person.toString());
        PersistenceEventData source = new PersistenceEventData(PersistenceEventType.READ, person);
        publisher.publishEvent(new PersistenceEvent(source));
    }   
    
    @PostUpdate
    public void afterUpdate(Person person) {
        LOGGER.info("[JPA-EVENT] UPDATE completed for PERSON: {} ", person.toString());
        PersistenceEventData source = new PersistenceEventData(PersistenceEventType.UPDATE, person);
        publisher.publishEvent(new PersistenceEvent(source));
    }    
    
    
    @PostRemove
    public void afterDelete(Person person) {
        LOGGER.info("[JPA-EVENT] DELETE completed for PERSON: {} ", person.toString());
        PersistenceEventData source = new PersistenceEventData(PersistenceEventType.DELETE, person);
        publisher.publishEvent(new PersistenceEvent(source));
    }
}

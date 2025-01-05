package com.zonesoft.addressbook.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.zonesoft.addressbook.entities.Person;

public interface PersonRepository extends  JpaRepository<Person, Long> {

}

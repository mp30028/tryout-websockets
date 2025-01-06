package com.zonesoft.addressbook.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.zonesoft.addressbook.entities.OtherNameType;

public interface OtherNameTypeRepository extends  JpaRepository<OtherNameType, Long> {

	List<OtherNameType> findByValue(String value);
}

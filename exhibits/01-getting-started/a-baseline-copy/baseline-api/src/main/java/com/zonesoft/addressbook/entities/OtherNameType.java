package com.zonesoft.addressbook.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="t_other_name_type")
public class OtherNameType {
	private Long id;
	private String value;

	public OtherNameType() {
		super();
	}
	
	public OtherNameType(Long id, String other_name_type) {
		super();
		this.id = id;
		this.value = other_name_type;
	}
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "other_name_type_id")
	public Long getId() {
		return this.id;
	}

	public void setId(Long otherNameTypeId) {
		this.id = otherNameTypeId;
	}

	@Column(name = "other_name_type")
	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}

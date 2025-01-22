package com.zonesoft.person.api.entities;

import java.util.Objects;

import org.bson.types.ObjectId;
import com.zonesoft.person.api.utils.ToStringHelper;


public class OtherName {
	private String id;
	private String value;
	private OtherNameType nameType;
	
	public OtherName(String id, String value, OtherNameType nameType) {
		super();
		this.id = this.checkId(id);
		this.value = value;
		this.nameType = nameType;
	}

	private String checkId(String id) {
		if (Objects.isNull(id)) {
			return (new ObjectId()).toHexString();
		}else {
			return id;
		}
	}

	public OtherName(String value, OtherNameType nameType) {
		super();
		this.id = this.checkId(null);
		this.value = value;
		this.nameType = nameType;
	}
	
	public OtherName() {
		super();
		this.id = this.checkId(null);
		this.value = null;
		this.nameType = null;		
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public OtherNameType getNameType() {
		return nameType;
	}

	public void setNameType(OtherNameType nameType) {
		this.nameType = nameType;
	}
	
	@Override
	public String toString() {		
		ToStringHelper helper = new ToStringHelper();
		return helper.begin()
			.wrLn("id", id)
			.wrLn("value", value)
			.wr("nameType", nameType)
		.end().build();		
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, nameType, value);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (!(obj instanceof OtherName))return false;
		OtherName other = (OtherName) obj;
		return 
				Objects.equals(id, other.id) && 
				nameType == other.nameType && 
				Objects.equals(value, other.value);
	}
	
}

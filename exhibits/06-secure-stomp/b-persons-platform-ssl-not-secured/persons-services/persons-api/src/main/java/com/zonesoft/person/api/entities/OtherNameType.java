package com.zonesoft.person.api.entities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum OtherNameType{
	MIDDLE_NAME,
	NICKNAME,
	ALIAS;
	
    public static List<OtherNameType> getAll(){
        return new ArrayList<OtherNameType>(Arrays.asList(values()));
    }
}

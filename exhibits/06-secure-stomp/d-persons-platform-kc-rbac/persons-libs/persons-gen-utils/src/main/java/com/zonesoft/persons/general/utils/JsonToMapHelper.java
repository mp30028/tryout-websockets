package com.zonesoft.persons.general.utils;

import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonToMapHelper {

	public static Map<String, Object> deserialise(String json){
    	ObjectMapper objectMapper = new ObjectMapper();
    	Map<String, Object> map = null;
		try {
			map = objectMapper.readValue(json, new TypeReference<Map<String,Object>>() {});
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return map;
	}
	
}

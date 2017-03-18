package com.iscas.dataflow.util;

import org.codehaus.jackson.map.ObjectMapper;

public final class ObjectMapperWrapper {
	
	private static final ObjectMapper objectMapper=new ObjectMapper();
	
	public static ObjectMapper getObjectMapper(){
		return objectMapper;
	}
	
}

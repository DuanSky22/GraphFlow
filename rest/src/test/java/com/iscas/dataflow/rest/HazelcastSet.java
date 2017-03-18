package com.iscas.dataflow.rest;

import java.util.Map;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

/** 
 * @author  DuanSky22@163.com 
 * @version 1.0 
 */

public class HazelcastSet {
	
	@SuppressWarnings("rawtypes")
	public static void main(String args[]){
		HazelcastInstance hs=Hazelcast.newHazelcastInstance();
		Map<String,Class> map=hs.getMap("class");
		map.put("Person", Person.class );
		
		Map<String,Person> personMap=hs.getMap("person");
		personMap.put("duansky", new Person());
	}

}

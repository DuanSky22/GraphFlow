package com.iscas.dataflow.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.ObjectMapper;

/** 
 * Json格式的字符串和Java对象之间的转换。
 * need modified. Cannot adapt for arrays.
 * @author  DuanSky22@163.com 
 * @version 1.0 
 */
public class JsonParser {
	
	private static Map<String,String> parseJsonNoArray(String json){
		Map<String,String> map=new HashMap<String,String>();
		if(json==null || json.length()<=2)
			return map;
		String[] pairs=json.substring(1, json.length()-1).split(",");
		for(int i=0;i<pairs.length;i++){
			String[] pair=pairs[i].split(":");
			
			if(pair[1].charAt(0)=='"')
				pair[1]=pair[1].substring(1);
			if(pair[1].charAt(pair[1].length()-1)=='"')
				pair[1]=pair[1].substring(0,pair[1].length()-1);
			
			map.put(pair[0].substring(1,pair[0].length()-1), pair[1]);
		}
		return map;
	}
	
	/**
	 * Parse json string to key-value pair. Pay attention that this method can only parse 
	 * single schema,For nest Json String it will failed,if the attribute contains "["、"]"
	 * it will also failed!
	 * @param json
	 * 	 String.
	 * @return
	 * 	Map pairs.
	 */
	@SuppressWarnings("unused")
	@Deprecated
	private static Map<String,String> parseJsonNotStable(String json){
		Map<String,String> result=new HashMap<String,String>();
		if(json.indexOf('[')==-1 || json.indexOf(']')==-1)
			return parseJsonNoArray(json);
		int start=1;int counter=0;
		List<String> list=new ArrayList<String>();
		for(int i=1;i<json.length();i++){
			char c = json.charAt(i);
			if(c=='['){
				counter++;
				continue;
			}
			if(c==']'){
				counter--;
				continue;
			}
			if(c==',' && counter==0){
				String pairs=json.substring(start,i);
				start=i+1;
			}
		}
		return result;
	}
	
	public static Map<String,String> parseJson(String json){
		Map<String,String> map=new HashMap<String,String>();
		//TODO wipper.
		ObjectMapper mapper=ObjectMapperWrapper.getObjectMapper();
		try {
			//System.out.println(json);
			org.codehaus.jackson.JsonParser jp = mapper.getJsonFactory().createJsonParser(json);
			JsonToken token=jp.getCurrentToken();
			while(token!=JsonToken.END_OBJECT){
				String attribute="";
				//TODO change to StringBuffer.
				StringBuffer value=new StringBuffer("");
				if(token==JsonToken.FIELD_NAME){
					attribute=jp.getCurrentName();//get attribute
					token=jp.nextToken();
					if(token==JsonToken.VALUE_NULL || token==JsonToken.VALUE_NUMBER_FLOAT
							||token==JsonToken.VALUE_FALSE || token==JsonToken.VALUE_NUMBER_INT
							||token==JsonToken.VALUE_STRING || token==JsonToken.VALUE_TRUE)
						value.append(jp.getText());
					if(token==JsonToken.START_ARRAY){
						token=jp.nextToken();
						value.append("[");
						while(token!=JsonToken.END_ARRAY){
							value.append(jp.getText()+",");
							token=jp.nextToken();
						}
						value=value.delete(value.length()-1, value.length()).append("]");
					}
				}
				if(attribute.length()!=0)
					map.put(attribute, value.toString());
				token=jp.nextToken();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return map;
	}
}

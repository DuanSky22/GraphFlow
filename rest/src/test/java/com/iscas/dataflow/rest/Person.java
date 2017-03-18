package com.iscas.dataflow.rest;

import java.io.Serializable;

/** 
 * @author  DuanSky22@163.com 
 * @version 1.0 
 */

public class Person implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name="duansky";
	private int age=24;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	
	@Override
	public String toString(){
		return "name:"+name+",age:"+age;
	}
}

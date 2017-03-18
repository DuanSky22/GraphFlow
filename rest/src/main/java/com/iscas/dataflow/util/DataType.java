package com.iscas.dataflow.util;
/** 
 * 数据类型的转换器
 * @author  DuanSky22@163.com 
 * @version 1.0 
 */

public class DataType {
	@SuppressWarnings("rawtypes")
	public static Class getType(String type){
		if(type.equalsIgnoreCase("java.lang.Integer") || type.equalsIgnoreCase("Integer") || type.equalsIgnoreCase("int"))
			return java.lang.Integer.class;
		else if(type.equalsIgnoreCase("java.lang.Double") || type.equalsIgnoreCase("Double")|| type.equalsIgnoreCase("double"))
			return java.lang.Double.class;
		else if(type.equalsIgnoreCase("java.lang.Long") || type.equalsIgnoreCase("Long")|| type.equalsIgnoreCase("long"))
			return java.lang.Long.class;
		else if(type.equalsIgnoreCase("java.lang.Character") || type.equalsIgnoreCase("Character")|| type.equalsIgnoreCase("char"))
			return java.lang.Character.class;
		else if(type.equalsIgnoreCase("java.lang.Boolean") || type.equalsIgnoreCase("Boolean"))
			return java.lang.Boolean.class;
		else if(type.equalsIgnoreCase("java.lang.Short") || type.equalsIgnoreCase("Short")|| type.equalsIgnoreCase("short"))
			return java.lang.Short.class;
		else if(type.equalsIgnoreCase("java.lang.Float") || type.equalsIgnoreCase("Float")|| type.equalsIgnoreCase("float"))
			return java.lang.Float.class;
		else if(type.equalsIgnoreCase("java.lang.Byte") || type.equalsIgnoreCase("Byte"))
			return java.lang.Byte.class;
		else if(type.equalsIgnoreCase("java.util.Date") || type.equalsIgnoreCase("Date"))
			return java.util.Date.class;
		else
			return java.lang.String.class;
	}
}

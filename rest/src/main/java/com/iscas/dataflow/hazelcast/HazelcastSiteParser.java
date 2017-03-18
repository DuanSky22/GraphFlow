package com.iscas.dataflow.hazelcast;

import java.util.Iterator;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * 爬取Hazelcast 客户端的配置信息。
 * @author DuanSky
 * @version 1.0 
 */
public class HazelcastSiteParser {
	
	/**
	 * serverAddressList: Hazelcast Server的IP地址列表
	 */
	String serverAddressList[];
	
	/**
	 * name: 用户名
	 */
	String name;
	
	/**
	 * password: 密码
	 */
	String password;
	
	/**
	 * instance:使用单例模式
	 */
	private static HazelcastSiteParser instance=null;
	
	/**
	 * getInstance:获取HazelcastSiteParser 的实例 
	 *
	 * @author DuanSky
	 * @return HazelcastSiteParser 的实例 
	 */
	public static HazelcastSiteParser getInstance(){
		if(instance == null){
			synchronized(HazelcastSiteParser.class){
				instance = new HazelcastSiteParser();
			}
		}
		return instance;
	}
	
	private HazelcastSiteParser(){
		parser();
	}
	
	/**
	 * parser: 从"hazelcast-site.xml"配置文件中爬取hazelcast客户端的配置信息
	 *
	 * @author DuanSky
	 */
	@SuppressWarnings("unchecked")
	private void parser(){
		SAXReader saxReader=new SAXReader();
		try {
			Document doc=saxReader.read(HazelcastSiteParser.class.
					getClassLoader().getResourceAsStream("hazelcast-site.xml"));
			Element root=doc.getRootElement();
			for( Iterator<Element>e = root.elementIterator(); e.hasNext();){
				parseElements(e.next());
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * getServerAddresses: 获取Hazelcast服务端的IP地址列表
	 *
	 * @author DuanSky
	 * @return Hazelcast服务端的IP地址列表
	 */
	public String[] getServerAddresses(){
		return serverAddressList;
	}
	
	/**
	 * getName:获取用户名 
	 *
	 * @author DuanSky
	 * @return 用户名
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * getPassword:获取密码 
	 *
	 * @author DuanSky
	 * @return 密码
	 */
	public String getPassword(){
		return password;
	}
	/**
	 * parseElements:解析客户端配置信息
	 *
	 * @author DuanSky
	 * @param e
	 */
	@SuppressWarnings("unchecked")
	private void parseElements(Element e){
		if(e.getName().equals("servers")){
			serverAddressList=e.getData().toString().trim().split(",");
		}
		if(e.getName().equals("group")){
			for( Iterator<Element>temp = e.elementIterator(); temp.hasNext();){
				Element t2=temp.next();
				if(t2.getName().equals("name")){
					name=t2.getData().toString().trim();
				}
				if(t2.getName().equals("password")){
					password=t2.getData().toString().trim();
				}
			}
		}
	}
}

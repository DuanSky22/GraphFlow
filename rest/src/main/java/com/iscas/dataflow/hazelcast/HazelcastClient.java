package com.iscas.dataflow.hazelcast;

import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;

/** 
 * Hazelcast的客户端
 * @author  DuanSky22@163.com 
 * @version 1.0 
 */
public class HazelcastClient {
	

	/**
	 * client: hazelcast的客户端实例
	 */
	private HazelcastInstance client=null;
	/**
	 * instance:采用单例模式
	 */
	private static HazelcastClient instance=null;
	
	private HazelcastClient(){
		
		ClientConfig clientConfig=new ClientConfig();
		
		HazelcastSiteParser hazelcastSiteParser=HazelcastSiteParser.getInstance();
		String[] serverAddresses=hazelcastSiteParser.getServerAddresses();
		String name=hazelcastSiteParser.getName();
		String password=hazelcastSiteParser.getPassword();
		
		clientConfig.getNetworkConfig().addAddress(serverAddresses);
		clientConfig.getGroupConfig().setName(name).setPassword(password);
		
		client=com.hazelcast.client.HazelcastClient.newHazelcastClient(clientConfig);
	}
	
	/**
	 * getInstance:获取Hazelcast客户端的实例 
	 *
	 * @author DuanSky
	 * @return HazelcastClient的实例
	 */
	public static HazelcastClient getInstance(){
		if(instance==null){
			synchronized(HazelcastClient.class){
				instance=new HazelcastClient();
			}
		}
		return instance;
	}
	
	public  HazelcastInstance getClient(){
		return client;
	}
}

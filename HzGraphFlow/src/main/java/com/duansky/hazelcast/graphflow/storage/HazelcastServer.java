package com.duansky.hazelcast.graphflow.storage;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;

import java.util.Timer;
import java.util.TimerTask;

/** 
 * Hzelcast 服务端
 * @author  DuanSky22@163.com 
 * @version 1.0 
 */

public class HazelcastServer {
	

	/**
	 * instance: 采用单例模式
	 */
	private static HazelcastServer instance=null;
	/**
	 * server:Hazelcast的服务端实例
	 */
	private HazelcastInstance server=null;
	

	
	public static void main(String args[]){
		HazelcastInstance hs=HazelcastServer.getInstance().getServer();
	}
		
	private HazelcastServer(){
		HazelcastSiteParser hazelcastSiteParser=HazelcastSiteParser.getInstance();
		
		String name=hazelcastSiteParser.getName();
		String password=hazelcastSiteParser.getPassword();
		
		Config config=new Config();
		config.getGroupConfig().setName(name).setPassword(password);
		
		server= Hazelcast.newHazelcastInstance(config);
	}
	
	/**
	 * getInstance:获取HazelcastServer实例
	 *
	 * @author DuanSky
	 * @return HazelcastServer实例
	 */
	public static HazelcastServer getInstance(){
		if(instance==null){
			synchronized(HazelcastServer.class){
				instance = new HazelcastServer();
			}
		}
		return instance;
	}
	
	/**
	 * getServer:获取Hazelcast 服务器实例 
	 *
	 * @author DuanSky
	 * @return Hazelcast 服务器实例
	 */
	public HazelcastInstance getServer(){
		return server;
	}

}

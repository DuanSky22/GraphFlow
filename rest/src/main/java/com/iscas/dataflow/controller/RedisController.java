package com.iscas.dataflow.controller;

import java.io.IOException;
import java.util.Map;

import com.hazelcast.core.IAtomicLong;
import com.iscas.dataflow.util.ObjectMapperWrapper;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.iscas.dataflow.hazelcast.HazelcastClient;

import static com.iscas.dataflow.util.DataFlowString.*;

/** 
 * 实时数据库的访问
 * @author  DuanSky22@163.com 
 * @version 1.0 
 */ 

@RestController
@RequestMapping(REDIS)
public class RedisController {

	/**
	 * hc:获取连接到Hazelcast Server 的客户端
	 */
	private static HazelcastInstance hc=HazelcastClient.getInstance().getClient();

	/**
	 * find: 返回Triangle Count 算法结果。
	 *
	 * @author DuanSky
	 * @param name triangle count名字
	 * @return 返回tc值
	 */
	@RequestMapping(value=FIND+"/tc"+"/{name}",method=RequestMethod.GET)
	@ResponseBody
	public String findTC(@PathVariable("name") String name){
		IAtomicLong count = hc.getAtomicLong(name);
		return count.get()+"";
	}
	
	/**
	 * find: 返回指定map名字，指定key值的数据
	 *
	 * @author DuanSky
	 * @param mapName map名
	 * @return 返回指定map的所有内容
	 */
	@RequestMapping(value=FIND+"/{mapName}",method=RequestMethod.GET)
	@ResponseBody
	public String findMap(@PathVariable("mapName") String mapName){
		Map<String,String> dataMap=hc.getMap(mapName);
		try {
			return ObjectMapperWrapper.getObjectMapper().writeValueAsString(dataMap);
		} catch (IOException e) {
			e.printStackTrace();
			return "Error";
		}
	}

	/**
	 * find: 返回指定map名字，指定key值的数据
	 *
	 * @author DuanSky
	 * @param mapName map名，对应kafka的topic,也对应HBase的table
	 * @param key 键值
	 * @return 返回指定map的指定key的消息内容
	 */
	@RequestMapping(value=FIND+"/{mapName}"+"/{key}",method=RequestMethod.GET)
	@ResponseBody
	public String find(
			@PathVariable("mapName") String mapName,
			@PathVariable("key") String key){
		Map dataMap=hc.getMap(mapName);
		String data=dataMap.get(Integer.parseInt(key))+"";
		return data;
	}

	/**
	 * update: 更新指定map，指定key值的数据
	 *
	 * @author DuanSky
	 * @param mapName map名，对应kafka的topic,也对应HBase的table
	 * @param key key值
	 * @param data 更新的内容
	 * @return 如果更新成功，则返回true；如果更新失败，则返回false
	 */
	@RequestMapping(value=UPDATE+"/{mapName}"+"/{key}")
    @ResponseBody
	public boolean update(
			@PathVariable("mapName") String mapName,
			@PathVariable("key") String key,
			@RequestParam(value="data",required = true) String data){
		Map<String,String> dataMap=hc.getMap(mapName);
		String redisKey=key;
		dataMap.put(redisKey, data);
		return true;
		
	}
	
	/**
	 * delete: 删除指定map，指定key值的数据
	 *
	 * @author DuanSky
	 * @param mapName map名，对应kafka的topic,也对应HBase的table
	 * @param key key值
	 * @return 如果删除成功，则返回true；如果删除失败，则返回false
	 */
	@RequestMapping(value=DELETE+"/{mapName}"+"/{key}")
    @ResponseBody
	public boolean delete(
			@PathVariable("mapName") String mapName,
			@PathVariable("key") String key){
		IMap<String,String> dataMap=hc.getMap(mapName);
		dataMap.remove(key);
		return true;
	}
}

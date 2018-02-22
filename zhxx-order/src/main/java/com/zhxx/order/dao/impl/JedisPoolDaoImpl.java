package com.zhxx.order.dao.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.zhxx.order.dao.JedisPoolDao.JedisPoolDao;

import redis.clients.jedis.JedisCluster;

@Repository
public class JedisPoolDaoImpl implements JedisPoolDao  {

	@Resource
	private JedisCluster jedisClusters;
	
	@Override
	public Boolean exists(String key) {
		return jedisClusters.exists(key);
	}

	@Override
	public String get(String key) {
		return jedisClusters.get(key);
	}

	@Override
	public Long del(String key) {
		return jedisClusters.del(key);
	}

	@Override
	public String set(String key, String value) {
		return jedisClusters.set(key, value);
	}
	
}

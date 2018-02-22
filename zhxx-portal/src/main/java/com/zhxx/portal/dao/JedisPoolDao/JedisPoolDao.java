package com.zhxx.portal.dao.JedisPoolDao;

public interface JedisPoolDao {
	/**
	 * 判断key是否存在
	 * @param key
	 * @return
	 */
	Boolean exists(String key);
	/**
	 * 取内容
	 * @param key
	 * @return
	 */
	String get(String key);
	/**
	 * 删除
	 * @param key
	 * @return
	 */
	Long del(String key);
	/**
	 * 新增
	 * @param key
	 * @param value
	 * @return
	 */
	String set(String key,String value);

}

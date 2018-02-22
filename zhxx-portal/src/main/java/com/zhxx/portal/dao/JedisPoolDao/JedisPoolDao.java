package com.zhxx.portal.dao.JedisPoolDao;

public interface JedisPoolDao {
	/**
	 * �ж�key�Ƿ����
	 * @param key
	 * @return
	 */
	Boolean exists(String key);
	/**
	 * ȡ����
	 * @param key
	 * @return
	 */
	String get(String key);
	/**
	 * ɾ��
	 * @param key
	 * @return
	 */
	Long del(String key);
	/**
	 * ����
	 * @param key
	 * @param value
	 * @return
	 */
	String set(String key,String value);

}

package com.zhxx.item.service.impl;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zhxx.commons.utils.JsonUtils;
import com.zhxx.dubbo.service.TbItemDescDubboService;
import com.zhxx.item.dao.JedisPoolDao.JedisPoolDao;
import com.zhxx.item.service.TbItemDescService;
import com.zhxx.pojo.TbItemDesc;

@Service
public class TbItemDescServiceImpl implements TbItemDescService {

	@Reference
	private TbItemDescDubboService tbItemDescDubboService;
	@Resource
	private JedisPoolDao jedisPoolDaoImpl;
	@Value("${redis.desc.key}")
	private String key;
	
	@Override
	public TbItemDesc selByItemId(long id) {
		String mykey = key+id;
		if(jedisPoolDaoImpl.exists(mykey)){
			String json = jedisPoolDaoImpl.get(mykey);
			if(json!=null&&!json.equals("")){
				return JsonUtils.jsonToPojo(json, TbItemDesc.class);
			}
		}
		TbItemDesc desc = tbItemDescDubboService.selByItemId(id);
		jedisPoolDaoImpl.set(mykey, JsonUtils.objectToJson(desc));
		return desc;
	}

}

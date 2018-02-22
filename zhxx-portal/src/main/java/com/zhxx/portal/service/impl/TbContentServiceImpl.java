package com.zhxx.portal.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zhxx.commons.utils.JsonUtils;
import com.zhxx.dubbo.service.TbContentDubboService;
import com.zhxx.pojo.TbContent;
import com.zhxx.portal.dao.JedisPoolDao.JedisPoolDao;
import com.zhxx.portal.service.TbContentService;

@Service
public class TbContentServiceImpl implements TbContentService {

	@Reference
	private TbContentDubboService tbContentDubboService;
	
	@Autowired
	private JedisPoolDao jedisPoolDaoImpl;
	
	@Value("${redis.bigpic.key}")
	private String key;
	@Value("${bigpic.id}")
	private long categoryId;
	
	@Override
	public String showBigPic() {
		String json = jedisPoolDaoImpl.get(key);
		if(!jedisPoolDaoImpl.exists(key)||!(json!=null&&!"".equals(json))) {
			List<TbContent> list = tbContentDubboService.selAllBy(categoryId);
			jedisPoolDaoImpl.set(key, JsonUtils.objectToJson(list));
			json = JsonUtils.objectToJson(list);
		}
		
		List<TbContent> list = JsonUtils.jsonToList(json, TbContent.class);
		List<Map<String,Object>> listMap = new ArrayList<>();
		
		for (TbContent content : list) {
			Map<String,Object> map = new HashMap<>();
			map.put("srcB", content.getPic2());
			map.put("height", 240);
			map.put("alt", "广告信息维护中");
			map.put("width", 550);
			map.put("src", content.getPic());
			map.put("widthB", 550);
			map.put("href", content.getUrl());
			map.put("heightB", 240);
			listMap.add(map);
		}
		
		return JsonUtils.objectToJson(listMap);
	}

}

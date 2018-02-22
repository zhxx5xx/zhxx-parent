package com.zhxx.item.service.impl;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zhxx.commons.pojo.TbItemChild;
import com.zhxx.commons.utils.JsonUtils;
import com.zhxx.dubbo.service.TbItemDubboService;
import com.zhxx.item.dao.JedisPoolDao.JedisPoolDao;
import com.zhxx.item.service.TbItemService;
import com.zhxx.pojo.TbItem;

@Service
public class TbItemServiceImpl implements TbItemService {

	
	@Reference
	private TbItemDubboService tbItemDubboService;
	
	@Resource
	private JedisPoolDao jedisPoolDaoImpl;
	
	@Value("${redis.item.key}")
	private String key;
	
	@Override
	public TbItemChild showItem(long id) {
		String mykey = key+id;
		if(jedisPoolDaoImpl.exists(mykey)){
			String json = jedisPoolDaoImpl.get(mykey);
			if(json!=null&&!json.equals("")){
				TbItemChild child = JsonUtils.jsonToPojo(json, TbItemChild.class);
				return child;
			}
		}
		TbItem tbItem = tbItemDubboService.selById(id);
		TbItemChild child = new TbItemChild();
		child.setBarcode(tbItem.getBarcode());
		child.setCid(tbItem.getCid());
		child.setCreated(tbItem.getCreated());
		child.setId(tbItem.getId());
		child.setImage(tbItem.getImage());
		child.setImages(tbItem.getImage()!=null&&!"".equals(tbItem.getImage())?tbItem.getImage().split(","):new String[1]);
		child.setNum(tbItem.getNum());
		child.setPrice(tbItem.getPrice());
		child.setSellPoint(tbItem.getSellPoint());
		child.setStatus(tbItem.getStatus());
		child.setTitle(tbItem.getTitle());
		child.setUpdated(tbItem.getUpdated());
		jedisPoolDaoImpl.set(mykey, JsonUtils.objectToJson(child));
		return child;
	}

}

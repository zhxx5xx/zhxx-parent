package com.zhxx.manager.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zhxx.commons.pojo.EasyUIDatagrid;
import com.zhxx.commons.pojo.Result;
import com.zhxx.commons.utils.IDUtils;
import com.zhxx.commons.utils.JsonUtils;
import com.zhxx.dubbo.service.TbContentDubboService;
import com.zhxx.manager.dao.JedisPoolDao.JedisPoolDao;
import com.zhxx.manager.service.TbContentService;
import com.zhxx.pojo.TbContent;

@Service
public class TbContentServiceImpl implements TbContentService {

	@Reference
	private TbContentDubboService tbContentDubboService;

	@Override
	public EasyUIDatagrid showContent(long categoryId, int page, int rows) {
		EasyUIDatagrid datagrid = new EasyUIDatagrid();
		datagrid.setRows(tbContentDubboService.selByCidPage(categoryId, page, rows));
		datagrid.setTotal(tbContentDubboService.selCountByCid(categoryId));
		return datagrid;
	}

	@Resource
	private JedisPoolDao jedisPoolDaoImpl;

	@Value("${redis.bigpic.key}")
	private String key;

	@Override
	public Result updContent(TbContent content) {
		Result re = new Result();
		content.setUpdated(new Date());
		// 直接更新
		int index = tbContentDubboService.updContent(content);
		if (index > 0) {
			// 看缓存中是否有数据
			if (jedisPoolDaoImpl.exists(key)) {
				// 取得json中的值
				String json = jedisPoolDaoImpl.get(key);
				if (json != null && !("".equals(json))) {
					// 声明一个变量来记录循环中i的值
					int listIndex = -1;
					// 将json字符串转换成java实体类对象
					List<TbContent> list = JsonUtils.jsonToList(json, TbContent.class);
					// 找出更新对象，在换成中存在的位置！
					for (int i = 0; i < list.size(); i++) {
						if ((long) list.get(i).getId() == (long) content.getId()) {
							listIndex = i;
						}
					}
					if (listIndex == -1) {
						// 此时数据库中的数据跟缓存中的数据不一致了！所以要删除缓存中的数据！ 缓存中的数据，必须跟数据库同步！
						jedisPoolDaoImpl.del(key);
					} else {
						list.remove(listIndex);
						// list.add("","") //直接将原来的替换成修改的内容。list.set("","")
						// 修改原下标的值。
						list.add(listIndex, content);
						// 将修改的对象，从新赋值给redis。
						jedisPoolDaoImpl.set(key, JsonUtils.objectToJson(list));
					}
				}
			}else {
				System.out.println("redis里没有数据");
			}
			re.setStatus(200);
		}
		return re;
	}

	@Override
	public Result save(TbContent content) {
		Result result = new Result();
		Date date = new Date();
		content.setCreated(date);
		content.setUpdated(date);
		content.setId(IDUtils.genItemId());
		int i = tbContentDubboService.insContent(content);
		if(i>0) {
			if(jedisPoolDaoImpl.exists(key)) {
				String json = jedisPoolDaoImpl.get(key);
				if(json!=null&&!"".equals(json)) {
					List<TbContent> list = JsonUtils.jsonToList(json, TbContent.class);
					list.add(content);
					String res = jedisPoolDaoImpl.set(key, JsonUtils.objectToJson(list));
					if("OK".equalsIgnoreCase(res)) {
						result.setStatus(200);
					}else {
						result.setStatus(400);
						jedisPoolDaoImpl.del(key);
					}
				}else {
					result.setStatus(200);
				}
			}else {
				result.setStatus(200);
			}
		}
		
		return result;
	}

	@Override
	public Result delByIds(String ids) {
		Result result = new Result();
		int index =0;
		String[] split = ids.split(",");
		for (String s : split) {
			index+=tbContentDubboService.delById(Long.parseLong(s));
		}
		if(index ==split.length) {
			if(jedisPoolDaoImpl.exists(key)){
				String json = jedisPoolDaoImpl.get(key);
				if(json!=null&&!"".equals(json)){
					List<TbContent> list = JsonUtils.jsonToList(json, TbContent.class);
					
					List<TbContent> listContent = new ArrayList<>();
					
					for (String s : split) {
						for(int i =0 ;i<list.size();i++){
							if((list.get(i).getId()+"").equals(s)) {
								listContent.add(list.get(i));
								break;
							}
						}
					}
					list.removeAll(listContent);
					jedisPoolDaoImpl.set(key, JsonUtils.objectToJson(list));
					result.setStatus(200);
				}else {
					result.setStatus(200);
				}
			}else {
				result.setStatus(200);
			}
		}else {
			result.setStatus(400);
		}
		return result;
	}

}

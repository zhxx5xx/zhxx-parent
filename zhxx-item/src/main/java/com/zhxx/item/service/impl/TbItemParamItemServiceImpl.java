package com.zhxx.item.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zhxx.commons.utils.JsonUtils;
import com.zhxx.dubbo.service.TbItemParamItemDubboService;
import com.zhxx.item.dao.JedisPoolDao.JedisPoolDao;
import com.zhxx.item.pojo.MyKeyValue;
import com.zhxx.item.pojo.ParamItem;
import com.zhxx.item.service.TbItemParamItemService;
import com.zhxx.pojo.TbItemParamItem;

@Service
public class TbItemParamItemServiceImpl implements TbItemParamItemService {
	@Reference
	private TbItemParamItemDubboService tbItemParamItemDubboService;

	@Resource
	private JedisPoolDao jedisPoolDaoImpl;

	@Value("${redis.param.key}")
	private String key;

	@Override
	public String showParam(long itemId) {
		// redis 中有数据，则从redis 中去，
		String mykey = key + itemId;
		if (jedisPoolDaoImpl.exists(mykey)) {
			// 取出json 数据
			String json = jedisPoolDaoImpl.get(mykey);
			// 判断json 是否存在
			if (json != null && !"".equals(json)) {
				return json;
			}
		}

		// 如果没有数据则从数据库中取得，并放到redis中
		TbItemParamItem itemParamItem = tbItemParamItemDubboService.selByItemId(itemId);
		String paramData = itemParamItem.getParamData();
		// 如何解决控制问题，如何避免空指针给页面带来的异常！
		if (paramData == null || "".equals(paramData)) {
			return "";
		} else {
			// 将以上数据转换成对象
			List<ParamItem> list = JsonUtils.jsonToList(paramData, ParamItem.class);
			// 将list 集合中的数据取出来，循环添加到一个table 表格上。在页面显示
			// 创建一个表格
			StringBuffer sf = new StringBuffer();
			sf.append("<table width='100%' style='color:#999999;'>");
			for (ParamItem paramItem : list) {
				List<MyKeyValue> listKV = paramItem.getParams();
				// 什么时候有大分组！
				for (int i = 0; i < listKV.size(); i++) {
					// 第一行应该有大分组grop
					if (i == 0) {
						sf.append("<tr>");
						sf.append("<td width='10%'>" + paramItem.getGroup() + "<td/>");
						sf.append("<td width='15%' align='right'>" + listKV.get(i).getK() + "</td>");
						sf.append("<td style='padding-left:30px;'>" + listKV.get(i).getV() + "</td>");
						sf.append("</tr>");
					} else {
						// 剩下的每一行没有大分组
						sf.append("<tr>");
						sf.append("<td width='10%'>");
						sf.append("</td>");
						sf.append("<td width='15%' align='right'>" + listKV.get(i).getK() + "</td>");
						sf.append("<td style='padding-left:30px;'>" + listKV.get(i).getV() + "</td>");
						sf.append("</tr>");
					}
				}
				// 添加一个分割线
				sf.append("<tr><td colspan='3'><hr style='color:#999999;'/></td></tr>");
			}
			sf.append("</table>");

			String string = sf.toString();
			// redis 中存储的啥：一个带数据的表格！
			jedisPoolDaoImpl.set(mykey, JsonUtils.objectToJson(string));
			return string;

		}

	}
}

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
		// redis �������ݣ����redis ��ȥ��
		String mykey = key + itemId;
		if (jedisPoolDaoImpl.exists(mykey)) {
			// ȡ��json ����
			String json = jedisPoolDaoImpl.get(mykey);
			// �ж�json �Ƿ����
			if (json != null && !"".equals(json)) {
				return json;
			}
		}

		// ���û������������ݿ���ȡ�ã����ŵ�redis��
		TbItemParamItem itemParamItem = tbItemParamItemDubboService.selByItemId(itemId);
		String paramData = itemParamItem.getParamData();
		// ��ν���������⣬��α����ָ���ҳ��������쳣��
		if (paramData == null || "".equals(paramData)) {
			return "";
		} else {
			// ����������ת���ɶ���
			List<ParamItem> list = JsonUtils.jsonToList(paramData, ParamItem.class);
			// ��list �����е�����ȡ������ѭ����ӵ�һ��table ����ϡ���ҳ����ʾ
			// ����һ�����
			StringBuffer sf = new StringBuffer();
			sf.append("<table width='100%' style='color:#999999;'>");
			for (ParamItem paramItem : list) {
				List<MyKeyValue> listKV = paramItem.getParams();
				// ʲôʱ���д���飡
				for (int i = 0; i < listKV.size(); i++) {
					// ��һ��Ӧ���д����grop
					if (i == 0) {
						sf.append("<tr>");
						sf.append("<td width='10%'>" + paramItem.getGroup() + "<td/>");
						sf.append("<td width='15%' align='right'>" + listKV.get(i).getK() + "</td>");
						sf.append("<td style='padding-left:30px;'>" + listKV.get(i).getV() + "</td>");
						sf.append("</tr>");
					} else {
						// ʣ�µ�ÿһ��û�д����
						sf.append("<tr>");
						sf.append("<td width='10%'>");
						sf.append("</td>");
						sf.append("<td width='15%' align='right'>" + listKV.get(i).getK() + "</td>");
						sf.append("<td style='padding-left:30px;'>" + listKV.get(i).getV() + "</td>");
						sf.append("</tr>");
					}
				}
				// ���һ���ָ���
				sf.append("<tr><td colspan='3'><hr style='color:#999999;'/></td></tr>");
			}
			sf.append("</table>");

			String string = sf.toString();
			// redis �д洢��ɶ��һ�������ݵı��
			jedisPoolDaoImpl.set(mykey, JsonUtils.objectToJson(string));
			return string;

		}

	}
}

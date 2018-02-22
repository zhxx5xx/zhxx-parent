package com.zhxx.order.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zhxx.commons.utils.IDUtils;
import com.zhxx.dubbo.service.TbOrderDubboService;
import com.zhxx.order.pojo.MyOrder;
import com.zhxx.order.service.TbOrderService;
import com.zhxx.pojo.TbOrder;
import com.zhxx.pojo.TbOrderItem;
import com.zhxx.pojo.TbOrderShipping;

@Service
public class TbOderServiceImpl implements TbOrderService {

	@Reference
	private TbOrderDubboService tbOrderDubboService;

	@Override
	public Map<String, Object> createOrder(MyOrder order) throws Exception {

		String orderId = IDUtils.genItemId() + "";
		Date date = new Date();

		TbOrder tbOrder = new TbOrder();
		tbOrder.setOrderId(orderId);
		tbOrder.setPayment(order.getPayment());
		tbOrder.setPaymentType(order.getPaymentType());
		tbOrder.setCreateTime(date);
		tbOrder.setUpdateTime(date);

		List<TbOrderItem> list = order.getOrderItems();
		for (TbOrderItem tbOrderItem : list) {
			tbOrderItem.setOrderId(orderId);
			tbOrderItem.setId(IDUtils.genItemId() + "");
		}

		TbOrderShipping shipping = order.getOrderShipping();
		shipping.setOrderId(orderId);
		shipping.setCreated(date);
		shipping.setUpdated(date);

		int i = tbOrderDubboService.insOrder(tbOrder, list, shipping);
		if(i!=1) {
			throw new Exception("´´½¨¶©µ¥Ê§°Ü");
		}

		Map<String, Object> map = new HashMap<>();
		map.put("orderId", orderId);

		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_MONTH, 2);
		map.put("date", c.getTime());
		return map;
	}

}

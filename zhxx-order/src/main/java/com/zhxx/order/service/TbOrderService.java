package com.zhxx.order.service;

import java.util.Map;

import com.zhxx.order.pojo.MyOrder;

public interface TbOrderService {
	Map<String,Object> createOrder(MyOrder order) throws Exception;
}

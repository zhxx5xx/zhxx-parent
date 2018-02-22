package com.zhxx.dubbo.service;

import java.util.List;

import com.zhxx.pojo.TbOrder;
import com.zhxx.pojo.TbOrderItem;
import com.zhxx.pojo.TbOrderShipping;

public interface TbOrderDubboService {
	int insOrder(TbOrder order,List<TbOrderItem> list,TbOrderShipping shipping) throws Exception;
}

package com.zhxx.dubbo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.zhxx.dubbo.service.TbOrderDubboService;
import com.zhxx.mapper.TbOrderItemMapper;
import com.zhxx.mapper.TbOrderMapper;
import com.zhxx.mapper.TbOrderShippingMapper;
import com.zhxx.pojo.TbOrder;
import com.zhxx.pojo.TbOrderItem;
import com.zhxx.pojo.TbOrderShipping;

public class TbOrderDubboServiceImpl implements TbOrderDubboService{

	
	@Autowired
	private TbOrderMapper tbOrderMapper;
	@Autowired
	private TbOrderItemMapper tbOrderItemMapper;
	@Autowired
	private TbOrderShippingMapper tbOrderShippingMapper;
	
	@Override
	public int insOrder(TbOrder order, List<TbOrderItem> list, TbOrderShipping shipping) throws Exception {
		int i = tbOrderMapper.insertSelective(order);
		for (TbOrderItem tbOrderItem : list) {
			i += tbOrderItemMapper.insertSelective(tbOrderItem);
		}
		i +=tbOrderShippingMapper.insertSelective(shipping);
		
		if(i==2+list.size()) {
			return 1;
		}else {
			throw new Exception("´´½¨¶©µ¥Ê§°Ü");
		}
		
	}

}

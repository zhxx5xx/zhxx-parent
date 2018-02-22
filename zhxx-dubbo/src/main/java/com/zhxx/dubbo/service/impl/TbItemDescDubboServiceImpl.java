package com.zhxx.dubbo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.zhxx.dubbo.service.TbItemDescDubboService;
import com.zhxx.mapper.TbItemDescMapper;
import com.zhxx.pojo.TbItemDesc;

public class TbItemDescDubboServiceImpl implements TbItemDescDubboService {

	@Autowired
	private TbItemDescMapper tbItemDescMapper;
	
	@Override
	public TbItemDesc selByItemId(long id) {
		
		return tbItemDescMapper.selectByPrimaryKey(id);
	}

}

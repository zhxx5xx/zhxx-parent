package com.zhxx.dubbo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhxx.commons.pojo.EasyUIDatagrid;
import com.zhxx.dubbo.service.TbItemDubboService;
import com.zhxx.mapper.TbItemMapper;
import com.zhxx.pojo.TbItem;
import com.zhxx.pojo.TbItemExample;

public class TbItemDubboServiceImpl implements TbItemDubboService {

	@Autowired
	private TbItemMapper tbItemMapper;

	@Override
	public EasyUIDatagrid showPage(int page, int rows) {
		EasyUIDatagrid datagrid = new EasyUIDatagrid();
		PageHelper.startPage(page, rows);
		List<TbItem> list = tbItemMapper.selectByExample(new TbItemExample());
		PageInfo<TbItem> pi = new PageInfo<>(list);
		datagrid.setRows(pi.getList());
		datagrid.setTotal(pi.getTotal());
		return datagrid;
	}

	@Override
	public int updStatusById(long id, byte status) {
		TbItem record = new TbItem();
		record.setId(id);
		record.setStatus(status);
		return tbItemMapper.updateByPrimaryKeySelective(record);
		
	}

	@Override
	public TbItem selById(long id) {
		return tbItemMapper.selectByPrimaryKey(id);
	}

}

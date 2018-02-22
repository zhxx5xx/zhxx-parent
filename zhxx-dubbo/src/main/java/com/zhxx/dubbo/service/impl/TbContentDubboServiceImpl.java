package com.zhxx.dubbo.service.impl;

import java.util.List;

import javax.annotation.Resource;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhxx.dubbo.service.TbContentDubboService;
import com.zhxx.mapper.TbContentMapper;
import com.zhxx.pojo.TbContent;
import com.zhxx.pojo.TbContentExample;

public class TbContentDubboServiceImpl implements TbContentDubboService {
	@Resource
	private TbContentMapper tbContentMapper;

	@Override
	public List<TbContent> selByCidPage(long cid, int page, int rows) {
		PageHelper.startPage(page, rows);
		TbContentExample example = new TbContentExample();
		example.createCriteria().andCategoryIdEqualTo(cid);
		List<TbContent> list = tbContentMapper.selectByExampleWithBLOBs(example);
		PageInfo<TbContent> pi = new PageInfo<>(list);
		return pi.getList();
	}

	@Override
	public long selCountByCid(long cid) {
		TbContentExample example = new TbContentExample();
		example.createCriteria().andCategoryIdEqualTo(cid);
		return tbContentMapper.countByExample(example);
	}

	@Override
	public int updContent(TbContent content) {
		
		return tbContentMapper.updateByPrimaryKeySelective(content);
	}

	@Override
	public List<TbContent> selAllBy(long category_id) {
		TbContentExample example = new TbContentExample();
		example.createCriteria().andCategoryIdEqualTo(category_id);
		return tbContentMapper.selectByExampleWithBLOBs(example);
	}

	@Override
	public int insContent(TbContent content) {
		return tbContentMapper.insertSelective(content);
	}

	@Override
	public int delById(Long id) {
		
		return tbContentMapper.deleteByPrimaryKey(id);
	}

}

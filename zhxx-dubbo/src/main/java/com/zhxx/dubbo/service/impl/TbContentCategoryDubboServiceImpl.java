package com.zhxx.dubbo.service.impl;

import java.util.List;

import javax.annotation.Resource;

import com.zhxx.dubbo.service.TbContentCategoryDubboService;
import com.zhxx.mapper.TbContentCategoryMapper;
import com.zhxx.pojo.TbContentCategory;
import com.zhxx.pojo.TbContentCategoryExample;

public class TbContentCategoryDubboServiceImpl implements TbContentCategoryDubboService {
	
	@Resource
	private TbContentCategoryMapper tbContentCategoryMapper;
	
	@Override
	public List<TbContentCategory> selByPid(long pid) {
		TbContentCategoryExample example = new TbContentCategoryExample();
		example.setOrderByClause("sort_order asc");
		example.createCriteria().andParentIdEqualTo(pid).andStatusEqualTo(1);
		return tbContentCategoryMapper.selectByExample(example);
	}

}

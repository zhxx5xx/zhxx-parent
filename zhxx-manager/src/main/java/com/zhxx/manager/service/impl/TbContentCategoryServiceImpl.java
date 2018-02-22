package com.zhxx.manager.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zhxx.commons.pojo.EasyUITree;
import com.zhxx.dubbo.service.TbContentCategoryDubboService;
import com.zhxx.manager.service.TbContentCategoryService;
import com.zhxx.pojo.TbContentCategory;

@Service
public class TbContentCategoryServiceImpl implements TbContentCategoryService {
	
	@Reference
	private TbContentCategoryDubboService tbContentCategoryDubboService;
	
	@Override
	public List<EasyUITree> selByPid(long pid) {
		List<EasyUITree> listTree = new ArrayList<>();
		
		List<TbContentCategory> list = tbContentCategoryDubboService.selByPid(pid);
		
		
		for (TbContentCategory c : list) {
			EasyUITree tree = new EasyUITree();
			tree.setId(c.getId());
			tree.setText(c.getName());
			tree.setState(c.getIsParent()?"closed":"open");
			listTree.add(tree);
		}
		return listTree;
	}

}

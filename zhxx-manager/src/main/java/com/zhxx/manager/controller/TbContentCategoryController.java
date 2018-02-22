package com.zhxx.manager.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zhxx.commons.pojo.EasyUITree;
import com.zhxx.manager.service.TbContentCategoryService;

@Controller
public class TbContentCategoryController {
	
	@Autowired
	private TbContentCategoryService tbContentCategoryService;
	
	
	@RequestMapping("content/category/list")
	@ResponseBody
	public List<EasyUITree> showCategory(@RequestParam(defaultValue="0") long id){
		return tbContentCategoryService.selByPid(id);
	}
}
	
	

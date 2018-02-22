package com.zhxx.manager.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zhxx.commons.pojo.EasyUIDatagrid;
import com.zhxx.commons.pojo.Result;
import com.zhxx.manager.service.TbContentService;
import com.zhxx.pojo.TbContent;

@Controller
public class TbContentController {
	@Resource
	private TbContentService tbContentService;
	
	@RequestMapping("content/query/list")
	@ResponseBody
	public EasyUIDatagrid showContent(long categoryId,int page,int rows){
		return tbContentService.showContent(categoryId, page, rows);
	}
	
	@RequestMapping("rest/content/edit")
	@ResponseBody
	public Result upd(TbContent content){
		return tbContentService.updContent(content);
	}
	
	@RequestMapping("content/save")
	@ResponseBody
	public Result save(TbContent content){
		return tbContentService.save(content);
	}
	
	@RequestMapping("content/delete")
	@ResponseBody
	public Result delete(String ids){
		return tbContentService.delByIds(ids);
	}
}

package com.zhxx.manager.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.zhxx.commons.pojo.EasyUIDatagrid;
import com.zhxx.commons.pojo.Result;
import com.zhxx.manager.service.TbItemService;

@Controller
public class TbItemController {
	
	@Autowired
	private TbItemService tbItemService;
	
	@RequestMapping("item/list")
	@ResponseBody
	public EasyUIDatagrid show(int page,int rows){
		return tbItemService.show(page, rows);
	}
	
	@RequestMapping("/rest/item/instock")
	@ResponseBody
	public Result instock(String ids){
		Result result = new Result();
		int i = tbItemService.updStatusById(ids, (byte)2);
		if(i>0) {
			result.setStatus(200);
		}else {
			result.setStatus(400);
		}
		return result;
	}
	
	@RequestMapping("/rest/item/reshelf")
	@ResponseBody
	public Result reshelf(String ids){
		Result result = new Result();
		int i = tbItemService.updStatusById(ids, (byte)1);
		if(i>0) {
			result.setStatus(200);
		}else {
			result.setStatus(400);
		}
		return result;
	}
	
	@RequestMapping("/rest/item/delete")
	@ResponseBody
	public Result delete(String ids){
		Result result = new Result();
		int i = tbItemService.updStatusById(ids, (byte)3);
		if(i>0) {
			result.setStatus(200);
		}else {
			result.setStatus(400);
		}
		return result;
	}
	
	@RequestMapping("pic/upload")
	@ResponseBody
	public Map<String,Object> upload(MultipartFile uploadFile){
		return tbItemService.upload(uploadFile);
	}
	

}

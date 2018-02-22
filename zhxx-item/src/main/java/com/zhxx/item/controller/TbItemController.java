package com.zhxx.item.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zhxx.item.service.TbItemService;

@Controller
public class TbItemController {
	
	@Autowired
	private TbItemService tbItemService;
	
	@RequestMapping("item/{id}.html")
	public String showItem(@PathVariable long id ,Model model) {
		model.addAttribute("item",tbItemService.showItem(id));
		return "item";
	}
	

}

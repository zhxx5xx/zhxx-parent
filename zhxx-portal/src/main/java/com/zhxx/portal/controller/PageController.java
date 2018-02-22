package com.zhxx.portal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zhxx.portal.service.TbContentService;

@Controller
public class PageController {

	@Autowired
	private TbContentService tbContentService;
	
	@RequestMapping("/")
	public String welcome(Model model){
		model.addAttribute("ad1", tbContentService.showBigPic());
		return "index";
	}
	
}

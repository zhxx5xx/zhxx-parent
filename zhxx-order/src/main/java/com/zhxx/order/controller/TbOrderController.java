package com.zhxx.order.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zhxx.order.pojo.MyOrder;
import com.zhxx.order.service.TbOrderService;

@Controller
public class TbOrderController {
	
	@Autowired
	private TbOrderService tbOrderService;
	
	@RequestMapping("order/create.html")
	public String createOrder(MyOrder order, Model model) {
		try {
			Map<String, Object> map = tbOrderService.createOrder(order);
			model.addAttribute("orderId", map.get("orderId"));
			model.addAttribute("date", map.get("date"));
			model.addAttribute("payment", order.getPayment());
			
		} catch (Exception e) {
			model.addAttribute("message",e.getMessage());
			return "error/exception";
		}
		return "success";
	}
}

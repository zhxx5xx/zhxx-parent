package com.zhxx.cart.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zhxx.cart.pojo.Cart;
import com.zhxx.cart.service.CartService;
import com.zhxx.commons.pojo.Result;

@Controller
public class CartController {

	@Autowired
	private CartService cartservice;

	@RequestMapping("/cart/add/{itemId}.html")
	public String addCart(@PathVariable("itemId") long id, @RequestParam(defaultValue="1")int num,HttpServletRequest request, Model model) {
		try {
			cartservice.addCart(id,num, request);
		} catch (Exception e) {
			model.addAttribute("message", e.getMessage());
			return "error/exception";
		}
		return "cartSuccess";
	}
	
	
	@RequestMapping("cart/cart.html")
	public String cart(HttpServletRequest request, Model model) {
		model.addAttribute("cartList", cartservice.showCart(request));
		return "cart";
	}
	
	@RequestMapping("cart/update/num/{itemId}/{num}.action")
	@ResponseBody
	public Result uotNum(@PathVariable("itemId")long itemId,@PathVariable("num") int num,HttpServletRequest request) {
		
		return cartservice.upItemNum(itemId, num, request);
	}

	
	@RequestMapping("cart/delete/{id}.action")
	@ResponseBody
	public Result delete(@PathVariable("id")long id,HttpServletRequest request) {
		
		return cartservice.del(id, request);
	}
	
	@RequestMapping("order/order-cart.html")
	public String showCartOrder(@RequestParam(value="id")List<Long> ids,Model model,HttpServletRequest request) {
		List<Cart> list = cartservice.showCartOrder(request, ids);
		if(list!=null) {
			model.addAttribute("cartList", list);
			return "order-cart";
		}else {
			model.addAttribute("message", "¿â´æ²»×ã");
			return "info/info";
		}
			
		
	}
	
	
}

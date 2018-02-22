package com.zhxx.cart.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.zhxx.cart.pojo.Cart;
import com.zhxx.commons.pojo.Result;

public interface CartService {
	void addCart(long itemId,int num,HttpServletRequest request) throws Exception;

	List<Cart> showCart(HttpServletRequest request);
	
	Result upItemNum(long itemId,int num,HttpServletRequest request);
	
	Result del(long itemId,HttpServletRequest request);
	
	List<Cart> showCartOrder(HttpServletRequest request,List<Long> ids);
}

package com.zhxx.cart.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.zhxx.cart.dao.JedisPoolDao.JedisPoolDao;
import com.zhxx.commons.utils.CookieUtils;

public class LoginInterceptor implements HandlerInterceptor{

	@Autowired
	private JedisPoolDao jedisPoolDaoImpl;
	
	@Override
	public boolean preHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2) throws Exception {
		String uuid = CookieUtils.getCookieValue(arg0, "TT_TOKEN");
		String key = "user:"+uuid;
		
		if(jedisPoolDaoImpl.exists(key)) {
			String json = jedisPoolDaoImpl.get(key);
			if(json!=null&&!"".equals(json)) {
				return true;
			}
		}
		arg1.sendRedirect("http://localhost:8084/user/showLogin");
		return false;
	}

	@Override
	public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
			throws Exception {
		// TODO Auto-generated method stub
		
	}
	
}

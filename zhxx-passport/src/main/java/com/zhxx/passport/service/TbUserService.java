package com.zhxx.passport.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zhxx.commons.pojo.Result;
import com.zhxx.pojo.TbUser;

public interface TbUserService {
	   Result login(TbUser tbUser,HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse);
	   
	   Result getToken(String ticket);
	   
	   Result logout(String ticket,HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse);
}

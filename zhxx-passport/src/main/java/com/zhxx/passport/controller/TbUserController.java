package com.zhxx.passport.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zhxx.commons.pojo.Result;
import com.zhxx.passport.service.TbUserService;
import com.zhxx.pojo.TbUser;

@Controller
public class TbUserController {

	@Autowired
	private TbUserService tbUserService;

	@RequestMapping("user/showLogin")
	public String showLogin(Model model, @RequestHeader("Referer") String refer) {
		if (refer == null) {
			model.addAttribute("redirect", "");
		}
		model.addAttribute("redirect", refer);
		return "login";
	}

	@RequestMapping("user/login")
	@ResponseBody
	public Result login(TbUser user, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
		Result result = tbUserService.login(user, httpServletRequest, httpServletResponse);
		return result;
	}

	@RequestMapping("user/token/{ticket}")
	@ResponseBody
	public Object show(@PathVariable("ticket") String ticket, String callback) {
		Result result = tbUserService.getToken(ticket);
		if (callback != null && !"".equals(callback)) {
			MappingJacksonValue mjv = new MappingJacksonValue(result);
			mjv.setJsonpFunction(callback);
			return mjv;
		}
		return result;
	}

	@RequestMapping("user/logout/{ticket}")
	@ResponseBody
	public Object logout(@PathVariable("ticket") String ticket, String callback, HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) {
		Result result = tbUserService.logout(ticket, httpServletRequest, httpServletResponse);
		if (callback == null||"".equals(callback)) {
			return result;
		} else {
			MappingJacksonValue mjv = new MappingJacksonValue(result);
			mjv.setJsonpFunction(callback);
			return mjv;
		}
	}

}

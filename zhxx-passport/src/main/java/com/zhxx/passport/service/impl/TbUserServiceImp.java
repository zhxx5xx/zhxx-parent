package com.zhxx.passport.service.impl;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zhxx.commons.pojo.Result;
import com.zhxx.commons.utils.CookieUtils;
import com.zhxx.commons.utils.JsonUtils;
import com.zhxx.dubbo.service.TbUserDubboService;
import com.zhxx.passport.dao.JedisPoolDao.JedisPoolDao;
import com.zhxx.passport.service.TbUserService;
import com.zhxx.pojo.TbUser;

@Service
public class TbUserServiceImp implements TbUserService {

	@Autowired
	private JedisPoolDao jedisPoolDaoImpl;

	@Reference
	private TbUserDubboService tbUserDubboService;

	@Value("${redis.user.key}")
	private String key;

	@Override
	public Result login(TbUser tbUser, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
		Result re = new Result();
		TbUser user = tbUserDubboService.selByUser(tbUser);
		if (user == null) {
			re.setStatus(400);
			return re;
		}

		String uuid = UUID.randomUUID().toString();
		CookieUtils.setCookie(httpServletRequest, httpServletResponse, "TT_TOKEN", uuid);

		jedisPoolDaoImpl.set(key + uuid, JsonUtils.objectToJson(user));

		re.setStatus(200);
		re.setMsg("OK");

		return re;
	}

	@Override
	public Result getToken(String ticket) {
		Result rs = new Result();
		if (jedisPoolDaoImpl.exists(key + ticket)) {
			String json = jedisPoolDaoImpl.get(key + ticket);
			if (json != null && !"".equals(json)) {
				rs.setStatus(200);
				rs.setData(JsonUtils.jsonToPojo(json, TbUser.class));
				rs.setMsg("OK");
			}else {
				rs.setStatus(400);
			}
		}else {
			rs.setStatus(400);
		}
		return rs;
	}

	@Override
	public Result logout(String ticket, HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) {
		Result rs = new Result();
		Long del = jedisPoolDaoImpl.del(key+ticket);
		CookieUtils.deleteCookie(httpServletRequest, httpServletResponse, "TT_TOKEN");
		if(del>0) {
			rs.setStatus(200);
			rs.setMsg("OK");
		}else {
			rs.setStatus(400);
		}
		return rs;
	}

}

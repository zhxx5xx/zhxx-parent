package com.zhxx.dubbo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;

import com.zhxx.dubbo.service.TbUserDubboService;
import com.zhxx.mapper.TbUserMapper;
import com.zhxx.pojo.TbUser;
import com.zhxx.pojo.TbUserExample;

public class TbUserDubboServiceImpl implements TbUserDubboService {

	@Autowired
	private TbUserMapper tbUserMapper;
	
	@Override
	public TbUser selByUser(TbUser tbUser) {
		TbUserExample example = new TbUserExample();
		
		example.createCriteria().andUsernameEqualTo(tbUser.getUsername()).
		andPasswordEqualTo(DigestUtils.md5DigestAsHex(tbUser.getPassword().getBytes()));
		
		List<TbUser> list = tbUserMapper.selectByExample(example);
		
		if(list!=null&&list.size()>0) {
			return list.get(0);
		}
		
		return null;
	}
	
	
}

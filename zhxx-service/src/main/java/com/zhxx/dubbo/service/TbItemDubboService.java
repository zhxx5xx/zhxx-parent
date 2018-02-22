package com.zhxx.dubbo.service;

import com.zhxx.commons.pojo.EasyUIDatagrid;
import com.zhxx.pojo.TbItem;

public interface TbItemDubboService {
	
	public EasyUIDatagrid showPage(int page, int rows);
	
	int updStatusById(long id,byte status);
	
	TbItem selById(long id);
}

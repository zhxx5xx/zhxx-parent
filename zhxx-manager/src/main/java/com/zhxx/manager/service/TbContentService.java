package com.zhxx.manager.service;

import com.zhxx.commons.pojo.EasyUIDatagrid;
import com.zhxx.commons.pojo.Result;
import com.zhxx.pojo.TbContent;

public interface TbContentService {
	EasyUIDatagrid showContent(long categoryId,int page,int rows);
	
	Result updContent(TbContent content);
	
	Result save(TbContent content);
	
	Result delByIds(String ids);
}

package com.zhxx.dubbo.service;

import java.util.List;

import com.zhxx.pojo.TbContent;

public interface TbContentDubboService {
	List<TbContent> selByCidPage(long cid, int page, int rows);

	long selCountByCid(long cid);
	
	int updContent(TbContent content);
	
	List<TbContent> selAllBy(long category_id);
	
	int insContent(TbContent content);
	
	int delById(Long id);
}

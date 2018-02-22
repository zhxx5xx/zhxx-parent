package com.zhxx.manager.service;

import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.zhxx.commons.pojo.EasyUIDatagrid;

public interface TbItemService {
	EasyUIDatagrid show(int page,int rows);
	
	int updStatusById(String ids,byte status);
	
	Map<String,Object> upload(MultipartFile file);
}

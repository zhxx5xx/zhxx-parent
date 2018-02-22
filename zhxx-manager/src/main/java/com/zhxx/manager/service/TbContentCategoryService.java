package com.zhxx.manager.service;

import java.util.List;

import com.zhxx.commons.pojo.EasyUITree;

public interface TbContentCategoryService {
	List<EasyUITree> selByPid(long pid);
}

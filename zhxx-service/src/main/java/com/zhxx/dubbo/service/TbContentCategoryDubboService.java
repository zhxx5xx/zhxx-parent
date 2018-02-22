package com.zhxx.dubbo.service;

import java.util.List;

import com.zhxx.pojo.TbContentCategory;

public interface TbContentCategoryDubboService {
	List<TbContentCategory> selByPid(long pid);

}

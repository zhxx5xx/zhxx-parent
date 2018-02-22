package com.zhxx.manager.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zhxx.commons.pojo.EasyUIDatagrid;
import com.zhxx.commons.utils.FtpUtil;
import com.zhxx.commons.utils.IDUtils;
import com.zhxx.dubbo.service.TbItemDubboService;
import com.zhxx.manager.service.TbItemService;

@Service
public class TbItemServiceImpl implements TbItemService {

	@Reference
	private TbItemDubboService tbItemDubboService;

	@Override
	public EasyUIDatagrid show(int page, int rows) {
		return tbItemDubboService.showPage(page, rows);
	}

	@Override
	public int updStatusById(String ids, byte status) {
		String[] split = ids.split(",");
		int flag = 0;
		for (String string : split) {
			flag += tbItemDubboService.updStatusById(Long.parseLong(string), status);
		}

		if (flag == split.length) {
			return 1;
		}
		return 0;
	}

	@Value("${vsftpd.host}")
	private String host;
	@Value("${vsftpd.port}")
	private int port;
	@Value("${vsftpd.username}")
	private String username;
	@Value("${vsftpd.password}")
	private String password;
	@Value("${vsftpd.basePath}")
	private String basePath;
	@Value("${vsftpd.filePath}")
	private String filePath;
	@Value("${nginx.url}")
	private String nginxUrl;

	@Override
	public Map<String, Object> upload(MultipartFile file) {
		Map<String, Object> map = new HashMap<>();
		String fileName = IDUtils.genImageName()
				+ file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
		InputStream input = null;
		try {
			input = file.getInputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		boolean b = FtpUtil.uploadFile(host, port, username, password, basePath, filePath, fileName, input);
		if(b) {
			map.put("error", 0);
			map.put("url", nginxUrl+fileName);
		}else {
			map.put("error", 1);
			map.put("message", "ÉÏ´«Í¼Æ¬Ê§°Ü");
		}
		return map;
	}

}















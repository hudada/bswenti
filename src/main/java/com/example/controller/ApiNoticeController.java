package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.bean.BaseBean;
import com.example.bean.NoticeBean;
import com.example.dao.NoticeDao;
import com.example.utils.ResultUtils;

@RestController
@RequestMapping(value = "/api/notice")
public class ApiNoticeController {

	@Autowired
	private NoticeDao noticeDao;

	/**
	 * 获取当前所有公告
	 * @return
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public BaseBean<List<NoticeBean>> getNotices() {
		List<NoticeBean> noticse=noticeDao.findAll();
		return ResultUtils.resultSucceed(noticse);
	}
}

package com.example.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.example.bean.AccountBean;
import com.example.bean.AdminBean;
import com.example.bean.BaseBean;
import com.example.bean.NoticeBean;
import com.example.bean.UserBean;
import com.example.dao.AccountDao;
import com.example.dao.AdminDao;
import com.example.dao.NoticeDao;
import com.example.dao.UserDao;
import com.example.utils.ResultUtils;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import com.example.WebSecurityConfig;

@Controller
@RequestMapping(value = "/page/notice")
public class PageNoticeController {

	@Autowired		
	private NoticeDao noticeDao;

	// 返回用户表信息
	@RequestMapping(value = "/table", method = RequestMethod.GET)
	public String table(ModelMap map) {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<NoticeBean> list = noticeDao.findAll();
		for(NoticeBean bean:list) {
			bean.setDate(format.format(new Date(Long.parseLong(bean.getDate()))));
		}
		map.addAttribute("list", list);
		return "notice/table";
	}

	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String addPage() {
		return "notice/add";
	}
	
	@RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
	public String editPage(@PathVariable String id,ModelMap map) {
		map.addAttribute("noticeBean", noticeDao.findOne(Long.parseLong(id)));
		return "notice/edit";
	}

	@RequestMapping(value = "/add", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public BaseBean<NoticeBean> addUser(@RequestBody NoticeBean noticeBean) {
		noticeBean.setDate(new Date().getTime()+"");
		return ResultUtils.resultSucceed(noticeDao.save(noticeBean));
	}

	@RequestMapping(value = "/edit/{id}", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public BaseBean<NoticeBean> editUser(@PathVariable String id,@RequestBody NoticeBean noticeBean) {
		noticeBean.setDate(new Date().getTime()+"");
		return ResultUtils.resultSucceed(noticeDao.save(noticeBean));
	}

	@RequestMapping(value = "/detele/{id}", method = RequestMethod.GET)
	@ResponseBody
	public BaseBean<NoticeBean> delUser(@PathVariable String id) {
		noticeDao.delete(Long.parseLong(id));
		return ResultUtils.resultSucceed("");
	}
}

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
import com.example.bean.PamentRecordBean;
import com.example.bean.UserBean;
import com.example.dao.AccountDao;
import com.example.dao.AdminDao;
import com.example.dao.UserDao;
import com.example.dao.pamentRecordDao;
import com.example.utils.ResultUtils;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import com.example.WebSecurityConfig;

@Controller
@RequestMapping(value = "/page/record")
public class PagePamentController {

	@Autowired
	private pamentRecordDao payDao;
	@Autowired
	private UserDao userDao;

	@RequestMapping(value = "/table", method = RequestMethod.GET)
	public String table(ModelMap map) {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<PamentRecordBean> list = payDao.findOrderByState();
		for(PamentRecordBean bean:list) {
			bean.setDate(format.format(new Date(Long.parseLong(bean.getDate()))));
		}
		map.addAttribute("list", list);
		return "pamentRecord/table";
	}

	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String addPage(ModelMap map) {
		map.addAttribute("list",userDao.findAll());
		return "pamentRecord/add";
	}
	
	@RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
	public String editPage(@PathVariable Long id,ModelMap map) {
		map.addAttribute("pament", payDao.findOne(id));
		return "pamentRecord/edit";
	}

	@RequestMapping(value = "/add", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public BaseBean<PamentRecordBean> addUser(@RequestBody PamentRecordBean pamentRecordBean) {
		pamentRecordBean.setDate(new Date().getTime()+"");
		return ResultUtils.resultSucceed(payDao.save(pamentRecordBean));
	}

	@RequestMapping(value = "/edit/{id}", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public BaseBean<PamentRecordBean> editUser(@RequestBody PamentRecordBean pamentRecordBean) {
		pamentRecordBean.setDate(new Date().getTime()+"");
		return ResultUtils.resultSucceed(payDao.save(pamentRecordBean));
	}

	@RequestMapping(value = "/detele/{id}", method = RequestMethod.GET)
	@ResponseBody
	public BaseBean<PamentRecordBean> delUser(@PathVariable Long id) {
		payDao.delete(id);
		return ResultUtils.resultSucceed("");
	}
}

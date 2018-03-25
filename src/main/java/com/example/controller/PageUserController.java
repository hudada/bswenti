package com.example.controller;

import static org.hamcrest.CoreMatchers.nullValue;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.hibernate.annotations.Sort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.example.bean.AdminBean;
import com.example.bean.BaseBean;
import com.example.bean.UserBean;
import com.example.dao.AdminDao;
import com.example.dao.UserDao;
import com.example.utils.ResultUtils;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import com.example.WebSecurityConfig;

@Controller
@RequestMapping(value = "/page/user")
public class PageUserController {

	@Autowired
	private UserDao userDao;	

	@RequestMapping(value = "/add", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public BaseBean<UserBean> add(@RequestBody UserBean userBean) {
		UserBean bean = userDao.findByName(userBean.getUserName());
		if (bean == null) {
			return ResultUtils.resultSucceed(userDao.save(userBean));
		}else {
			return ResultUtils.resultError("当前账号已存在");
		}
	}
	
	@RequestMapping(value = "/edit/{id}", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public BaseBean<UserBean> edit(@PathVariable String id,@RequestBody UserBean userBean) {
		UserBean bean = userDao.findOne(Long.parseLong(id));
		if (!bean.getUserName().equals(userBean.getUserName()) && 
				userDao.findByName(userBean.getUserName()) != null) {
			return ResultUtils.resultError("当前账号已存在");
		}else {
			userBean.setId(Long.parseLong(id));
			return ResultUtils.resultSucceed(userDao.save(userBean));
		}
	}
	
	@RequestMapping(value = "/detele/{id}", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public BaseBean<UserBean> detele(@PathVariable String id) {
		userDao.delete(Long.parseLong(id));
		return ResultUtils.resultSucceed("");
	}
}

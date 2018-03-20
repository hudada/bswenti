package com.example.controller;

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

import com.example.bean.AccountBean;
import com.example.bean.AdminBean;
import com.example.bean.BaseBean;
import com.example.bean.UserBean;
import com.example.dao.AccountDao;
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
	@Autowired
	private AccountDao accountDao;
	private int size = 50;

	@RequestMapping(value = "/table/{page}", method = RequestMethod.GET)
	public String table(@PathVariable int page,ModelMap map) {
		Pageable p = new PageRequest(page, size);
		map.addAttribute("list", userDao.findAll(p));
		map.addAttribute("page", page);
		map.addAttribute("sum", 1);
		return "user/table";
	}

	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String addPage() {
		return "user/add";
	}
	
	@RequestMapping(value = "/edit/{number}", method = RequestMethod.GET)
	public String editPage(@PathVariable String number,ModelMap map) {
		map.addAttribute("userBean", userDao.findUserByNumber(number));
		return "user/edit";
	}

	// 添加用户
	@RequestMapping(value = "/add", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public BaseBean<UserBean> addUser(@RequestBody UserBean userBean) {
		if (accountDao.findAccountByNumber(userBean.getNumber()) == null) {
			AccountBean accountBean = new AccountBean();
			accountBean.setNumber(userBean.getNumber());
			accountBean.setPwd("111111");
			AccountBean save = accountDao.save(accountBean);
			return ResultUtils.resultSucceed(userDao.save(userBean));
		} else {
			return ResultUtils.resultError("用户名已存在");
		}
	}

	// 修改用户
	@RequestMapping(value = "/edit/{number}", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public BaseBean<UserBean> editUser(@PathVariable String number,@RequestBody UserBean userBean) {
		UserBean user = userDao.findUserByNumber(number);
		user.setName(userBean.getName());
		user.setSex(userBean.getSex());
		user.setTel(userBean.getTel());
		user.setBalance(userBean.getBalance());
		user.setDong(userBean.getDong());
		user.setDan(userBean.getDan());
		user.setHao(userBean.getHao());
		return ResultUtils.resultSucceed(userDao.save(user));
	}

	// 删除用户
	@RequestMapping(value = "/detele/{number}", method = RequestMethod.GET)
	@ResponseBody
	public BaseBean<UserBean> delUser(@PathVariable String number) {
		AccountBean a = accountDao.findAccountByNumber(number);
		accountDao.delete(a);
		UserBean user = userDao.findUserByNumber(number);
		userDao.delete(user);
		return ResultUtils.resultSucceed("");
	}
}

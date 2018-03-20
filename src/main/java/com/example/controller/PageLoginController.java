package com.example.controller;

import java.util.ArrayList;
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
import com.example.bean.UserBean;
import com.example.dao.AccountDao;
import com.example.dao.AdminDao;
import com.example.dao.UserDao;
import com.example.utils.ResultUtils;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.spring.web.json.Json;

import com.example.WebSecurityConfig;

@Controller
public class PageLoginController {

	@Autowired
	private AdminDao adminDao;

	// 返回登录页面
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String loginPage(ModelMap map) {
		return "login";
	}

	// 登录接口
	@RequestMapping(value = "/dologin", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public BaseBean<String> userLogin(@RequestBody AdminBean adminBean, HttpSession session) {
		AdminBean admin = adminDao.findAdminBeanByNumber(adminBean.getAdminCode());
		if (admin != null) {
			if (admin.getPwd().equals(adminBean.getPwd())) {
				session.setAttribute(WebSecurityConfig.SESSION_KEY, adminBean);
				return ResultUtils.resultSucceed("登陆成功");
			} else {
				return ResultUtils.resultError("密码错误！");
			}
		} else {
			return ResultUtils.resultError("账户不存在！");
		}
	}

	// 退出登录接口
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String loginOut(HttpSession session) {
		session.removeAttribute(WebSecurityConfig.SESSION_KEY);
		return "redirect:/login";
	}

	// 返回管理首页
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index(ModelMap map, HttpSession session) {
		AdminBean admin = (AdminBean) session.getAttribute(WebSecurityConfig.SESSION_KEY);
		map.addAttribute("name", admin.getAdminCode());
		return "index";
	}

	// 返回头部
	@RequestMapping(value = "/top", method = RequestMethod.GET)
	public String top(ModelMap map, HttpSession session) {
		AdminBean admin = (AdminBean) session.getAttribute(WebSecurityConfig.SESSION_KEY);
		map.addAttribute("name", admin.getAdminCode());
		return "top";
	}

	// 返回左侧
	@RequestMapping(value = "/left", method = RequestMethod.GET)
	public String left(ModelMap map, HttpSession session) {
		return "left";
	}
}

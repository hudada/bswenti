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

import com.example.bean.AdminBean;
import com.example.bean.BaseBean;
import com.example.bean.QuestionBean;
import com.example.bean.ReplayBean;
import com.example.bean.UserBean;
import com.example.dao.AdminDao;
import com.example.dao.QuestionDao;
import com.example.dao.ReplayDao;
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
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private QuestionDao questionDao;

	@Autowired
	private ReplayDao replayDao;

	// 返回登录页面
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String loginPage(ModelMap map) {
		map.addAttribute("title","企业后台管理");
		return "newlogin";
	}

	// 登录接口
	@RequestMapping(value = "/dologin", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public BaseBean<String> userLogin(@RequestBody AdminBean adminBean, HttpSession session) {
		AdminBean admin = adminDao.findByNameAndPwd(adminBean.getUserName(), adminBean.getPwd());
		if (admin != null) {
			session.setAttribute(WebSecurityConfig.SESSION_KEY, adminBean);
			return ResultUtils.resultSucceed("登陆成功");
		} else {
			return ResultUtils.resultError("账号或密码错误");
		}
	}

	// 退出登录接口
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String loginOut(HttpSession session) {
		session.removeAttribute(WebSecurityConfig.SESSION_KEY);
		return "redirect:/login";
	}

	private ModelMap getPub(ModelMap map, HttpSession session,int position) {
		AdminBean admin = (AdminBean) session.getAttribute(WebSecurityConfig.SESSION_KEY);
		map.addAttribute("name", admin.getUserName());
		map.addAttribute("title","企业后台管理");
		map.addAttribute("left",""
				+ "<li>"
					+ "<a href=\"#\">用户</a>"
				+ "</li>"
				+ "<li "+isActive(1,position)+">"
					+ "<a href=\"/userAdd\">"
					+ "<i class=\"icon-chevron-right\"></i>新增用户</a>"
				+ "</li>"
				+ "<li "+isActive(2,position)+">"
					+ "<a href=\"/userManager\">"
					+ "<i class=\"icon-chevron-right\"></i>用户列表</a>"
				+ "</li>"
				+ "<li>"
					+ "<a href=\"#\">问题</a>"	
				+ "</li>"
				+ "<li "+isActive(3,position)+">"
					+ "<a href=\"/question/0/-1\">"
					+ "<span class=\"badge badge-info pull-right\">"+getQuestionCount(0)+"</span>待解决</a>"
				+ "</li>"
				+ "<li "+isActive(4,position)+">"
					+ "<a href=\"/question/1/-1\">"
					+ "<span class=\"badge badge-info pull-right\">"+getQuestionCount(1)+"</span>解决中</a>"
				+ "</li>"
				+ "<li "+isActive(5,position)+">"
					+ "<a href=\"/question/2/-1\">"
					+ "<span class=\"badge badge-info pull-right\">"+getQuestionCount(2)+"</span>已解决</a>"
				+ "</li>");
		return map;
	}

	private int getQuestionCount(int i) {
		return questionDao.findByStatus(i);
	}

	private String isActive(int curr,int position) {
		return position==curr?"class=\"active\"":"";
	}

	// 返回管理首页
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index(ModelMap map, HttpSession session) {
		getPub(map, session,0);
		return "newindex";
	}

	// ----------------------------------------------------------------
	@RequestMapping(value = "/userManager", method = RequestMethod.GET)
	public String userManager(ModelMap map, HttpSession session) {
		getPub(map, session,2);
		List<UserBean> list = userDao.findOrderLid();
		for (UserBean bean : list) {
			if (bean.getLid() == 1) {
				try {
					bean.setScore(questionDao.findScoreByTeam(bean.getTeam()));
				}catch (Exception e) {
					bean.setScore(0d);
				}
			}
		}
		map.addAttribute("list", list);
		return "user/newtable";
	}
	
	@RequestMapping(value = "/userEdit/{id}", method = RequestMethod.GET)
	public String editUser(@PathVariable String id, ModelMap map, HttpSession session) {
		getPub(map, session,2);
		map.addAttribute("userBean", userDao.findOne(Long.parseLong(id)));
		return "user/editform";
	}
	
	@RequestMapping(value = "/userAdd", method = RequestMethod.GET)
	public String userAdd(ModelMap map, HttpSession session) {
		getPub(map, session,1);
		return "user/addform";
	}


	@RequestMapping(value = "/question/{status}/{type}", method = RequestMethod.GET)
	public String question(ModelMap map, HttpSession session,
			@PathVariable String status,@PathVariable String type) {
		
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		int statusInt = Integer.parseInt(status);
		int typeInt = Integer.parseInt(type);
		List<QuestionBean> list;
		if (typeInt <0) {
			list = questionDao.findListByStatusAndType(statusInt);
		}else {
			list = questionDao.findListByStatusAndType(statusInt,typeInt);
		}
		for (QuestionBean questionBean : list) {
			questionBean.setImg("/api/question/"+questionBean.getImg());
			String name = userDao.findOne(questionBean.getUid()).getUserName();
			questionBean.setName(name);
			questionBean.setTimeStr(format.format(new Date(questionBean.getTime())));
		}
		map.addAttribute("list", list);
		
		String url = "";
		switch (statusInt) {
		case 0:
			getPub(map, session,3);
			map.addAttribute("active", 3);
			url= "question/newtable0";
			break;
		case 1:
			getPub(map, session,4);
			map.addAttribute("active", 4);
			url= "question/newtable1";
			break;
		case 2:
			getPub(map, session,5);
			map.addAttribute("active", 5);
			url= "question/newtable2";
			break;
		}
		return url;
	}

	@RequestMapping(value = "/replay/{active}/{id}", method = RequestMethod.GET)
	public String replay(ModelMap map, HttpSession session,
			@PathVariable String active,@PathVariable String id) {
		int activeInt = Integer.parseInt(active);
		long idInt = Long.parseLong(id);
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<ReplayBean> replayBeans = replayDao.findByQid(idInt);
		for (ReplayBean replayBean : replayBeans) {
			String name = userDao.findOne(replayBean.getUid()).getUserName();
			replayBean.setName(name);
			replayBean.setTimeStr(format.format(new Date(replayBean.getTime())));
		}
		getPub(map, session,activeInt);
		map.addAttribute("list", replayBeans);
		return "question/replay";
	}
}

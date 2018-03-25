package com.example.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.bean.BaseBean;
import com.example.bean.UserBean;
import com.example.dao.QuestionDao;
import com.example.dao.UserDao;
import com.example.utils.ResultUtils;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/api/user")
public class ApiUserController {

	@Autowired
	private UserDao userDao;
	@Autowired
	private QuestionDao questionDao;

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public BaseBean<UserBean> login(HttpServletRequest request) {
		String userName = request.getParameter("userName");
		String pwd = request.getParameter("pwd");
		UserBean bean = userDao.findByNameAndPwd(userName, pwd);
		if (bean == null) {
			return ResultUtils.resultError("用户名或密码错误");
		} else {
			try {
				bean.setScore(questionDao.findScoreByTeam(bean.getTeam()));
			}catch (Exception e) {
				bean.setScore(0d);
			}
			
			return ResultUtils.resultSucceed(bean);
		}
	}

	@RequestMapping(value = "/info", method = RequestMethod.GET)
	public BaseBean<UserBean> info(HttpServletRequest request) {
		Long uid = Long.parseLong(request.getParameter("uid"));
		UserBean bean = userDao.findOne(uid);
		bean.setScore(questionDao.findScoreByTeam(bean.getTeam()));
		return ResultUtils.resultSucceed(bean);
	}

}

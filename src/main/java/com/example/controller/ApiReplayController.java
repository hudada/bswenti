package com.example.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;

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
import com.example.bean.QuestionBean;
import com.example.bean.ReplayBean;
import com.example.bean.UserBean;
import com.example.dao.QuestionDao;
import com.example.dao.ReplayDao;
import com.example.dao.UserDao;
import com.example.utils.ResultUtils;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/api/replay")
public class ApiReplayController {

	@Autowired
	private UserDao userDao;
	@Autowired
	private QuestionDao questionDao;
	@Autowired
	private ReplayDao replayDao;
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public BaseBean<List<ReplayBean>> list(HttpServletRequest request) {
		Long qid = Long.parseLong(request.getParameter("qid"));
		return ResultUtils.resultSucceed(replayDao.findByQid(qid));
	}
	
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public BaseBean<ReplayBean> delete(HttpServletRequest request) {
		Long id = Long.parseLong(request.getParameter("id"));
		replayDao.delete(id);
		return ResultUtils.resultSucceed("");
	}
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public BaseBean<ReplayBean> add(HttpServletRequest request) {
		String info = request.getParameter("info");
		Long qid = Long.parseLong(request.getParameter("qid"));
		Long uid = Long.parseLong(request.getParameter("uid"));
		int status = Integer.parseInt(request.getParameter("status"));

		QuestionBean questionBean = questionDao.findOne(qid);
		questionBean.setStatus(status);
		questionDao.save(questionBean);
		
		ReplayBean bean = new ReplayBean();
		bean.setInfo(info);
		bean.setQid(qid);
		bean.setTime(new Date().getTime());
		bean.setUid(uid);
		
		return ResultUtils.resultSucceed(replayDao.save(bean));
	}

}

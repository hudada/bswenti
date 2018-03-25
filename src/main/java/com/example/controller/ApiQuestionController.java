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
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.bean.BaseBean;
import com.example.bean.QuestionBean;
import com.example.bean.UserBean;
import com.example.dao.QuestionDao;
import com.example.dao.UserDao;
import com.example.utils.ResultUtils;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/api/question")
public class ApiQuestionController {

	@Autowired
	private QuestionDao questionDao;
	
	@Value("${bs.imagesPath}")
	private String location;
	@Autowired
	private ResourceLoader resourceLoader;
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public BaseBean<List<QuestionBean>> getList(HttpServletRequest request) {
		Long uid = Long.parseLong(request.getParameter("uid"));
		int status = Integer.parseInt(request.getParameter("status"));
		String key = request.getParameter("key");
		if(status<0) {
			return ResultUtils.resultSucceed(questionDao.findByKeyOrderStatus(uid,key));
		}else {
			return ResultUtils.resultSucceed(questionDao.findByKeyOrderStatus(uid,status,key));
		}
	}
	
	@RequestMapping(value = "/listM", method = RequestMethod.GET)
	public BaseBean<List<QuestionBean>> getListM(HttpServletRequest request) {
		int team = Integer.parseInt(request.getParameter("team"));
		int status = Integer.parseInt(request.getParameter("status"));
		String key = request.getParameter("key");
		if (status==-2) {
			return ResultUtils.resultSucceed(questionDao.findListByTean(team));
		}else if (status == -1) {
			return ResultUtils.resultSucceed(questionDao.findListByTean(team,key));
		}else {
			return ResultUtils.resultSucceed(questionDao.findListByTean(team,status,key));
		}
		
	}
	
	@RequestMapping(value = "/score", method = RequestMethod.POST)
	public BaseBean<QuestionBean> score(HttpServletRequest request) {
		Long id = Long.parseLong(request.getParameter("id"));
		double score = Double.parseDouble(request.getParameter("score"));
		QuestionBean bean = questionDao.findOne(id);
		bean.setScore(score);
		return ResultUtils.resultSucceed(questionDao.save(bean));
	}
	
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public BaseBean<QuestionBean> detele(HttpServletRequest request) {
		Long id = Long.parseLong(request.getParameter("id"));
		questionDao.delete(id);
		return ResultUtils.resultSucceed("");
	}

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public BaseBean<QuestionBean> uploadImg(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
		Long time = Long.parseLong(request.getParameter("time"));
		String info = request.getParameter("info");
		int type = Integer.parseInt(request.getParameter("type"));
		String addr = request.getParameter("addr");
		int team = Integer.parseInt(request.getParameter("team"));
		Long uid = Long.parseLong(request.getParameter("uid"));
		
		if (!file.isEmpty()) {
			try {
				String path = uid + "_" + System.currentTimeMillis() + "." + file.getOriginalFilename().split("\\.")[1];
				
				File root = new File(location);
		        if (!root.exists()) {
		        	root.mkdirs();
		        }
				
				
				Files.copy(file.getInputStream(), Paths.get(location, path));

				QuestionBean bean = new QuestionBean();
				bean.setTime(time);
				bean.setInfo(info);
				bean.setType(type);
				bean.setAddr(addr);
				bean.setTeam(team);
				bean.setUid(uid);
				bean.setImg("/img/"+path);
				return ResultUtils.resultSucceed(questionDao.save(bean));
			} catch (IOException | RuntimeException e) {
				return ResultUtils.resultError("");
			}
		} else {
			return ResultUtils.resultError("");
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/img/{filename:.+}")
	public ResponseEntity<?> getFile(@PathVariable String filename) {
		try {
			return ResponseEntity.ok(resourceLoader.getResource("file:" + Paths.get(location, filename).toString()));
		} catch (Exception e) {
			return ResponseEntity.notFound().build();
		}
	}
}

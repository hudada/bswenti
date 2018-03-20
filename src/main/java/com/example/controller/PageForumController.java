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
import com.example.bean.ForumAndReplyCount;
import com.example.bean.ForumBean;
import com.example.bean.PamentRecordBean;
import com.example.bean.ReplyBean;
import com.example.bean.UserBean;
import com.example.dao.AccountDao;
import com.example.dao.AdminDao;
import com.example.dao.ForumDao;
import com.example.dao.ReplyDao;
import com.example.dao.UserDao;
import com.example.utils.ResultUtils;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import com.example.WebSecurityConfig;

@Controller
@RequestMapping(value = "/page/forum")
public class PageForumController {

	@Autowired
	private ForumDao forumDao;
	@Autowired
	private ReplyDao replyDao;

	@RequestMapping(value = "/table", method = RequestMethod.GET)
	public String table(ModelMap map) {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<ForumAndReplyCount> result = new ArrayList<>();
		List<ForumBean> forums=forumDao.findAll();
		for (ForumBean forumBean : forums) {
			ForumAndReplyCount forumAndReplyCount = new ForumAndReplyCount();
			forumAndReplyCount.setId(forumBean.getId());
			forumAndReplyCount.setTitle(forumBean.getTitle());
			forumAndReplyCount.setInfo(forumBean.getInfo());
			forumAndReplyCount.setNumber(forumBean.getNumber());
			forumAndReplyCount.setDate(format.format(new Date(Long.parseLong(forumBean.getDate()))));
			forumAndReplyCount.setState(forumBean.getState());
			int count = replyDao.findCountByForumId(forumBean.getId()+"");
			forumAndReplyCount.setCount(count);
			result.add(forumAndReplyCount);
		}
		map.addAttribute("list", result);
		return "forum/table";
	}

	@RequestMapping(value = "/detele/{id}", method = RequestMethod.GET)
	@ResponseBody
	public BaseBean<ForumBean> delUser(@PathVariable String id) {
		List<ReplyBean> list = replyDao.findReplayByForumId(id);
		for(ReplyBean bean : list) {
			replyDao.delete(bean.getId());
		}
		forumDao.delete(Long.parseLong(id));
		return ResultUtils.resultSucceed("");
	}
}

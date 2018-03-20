package com.example.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.bean.BaseBean;
import com.example.bean.ForumAndReplyCount;
import com.example.bean.ForumBean;
import com.example.bean.ReplyBean;
import com.example.bean.UserBean;
import com.example.dao.ForumDao;
import com.example.dao.ReplyDao;
import com.example.utils.ResultUtils;
import com.mysql.fabric.xmlrpc.base.Data;

@RestController
@RequestMapping(value = "/api/forum")
public class ApiForumController {

	@Autowired
	private ForumDao forumDao;
	@Autowired
	private ReplyDao replyDao;
	

	/**
	 * 获取当前所有文章（及回复数）
	 * @return
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public BaseBean<List<ForumAndReplyCount>> getNotices() {

		List<ForumAndReplyCount> result = new ArrayList<>();
		List<ForumBean> forums=forumDao.findAll();
		for (ForumBean forumBean : forums) {
			ForumAndReplyCount forumAndReplyCount = new ForumAndReplyCount();
			forumAndReplyCount.setId(forumBean.getId());
			forumAndReplyCount.setTitle(forumBean.getTitle());
			forumAndReplyCount.setInfo(forumBean.getInfo());
			forumAndReplyCount.setNumber(forumBean.getNumber());
			forumAndReplyCount.setDate(forumBean.getDate());
			forumAndReplyCount.setState(forumBean.getState());
			int count = replyDao.findCountByForumId(forumBean.getId()+"");
			forumAndReplyCount.setCount(count);
			result.add(forumAndReplyCount);
		}
		return ResultUtils.resultSucceed(result);
	}
	
	/**
	 * 获取所有回复
	 * @return
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public BaseBean<List<ReplyBean>> getReplays(@PathVariable String id) {

		List<ReplyBean> result=replyDao.findReplayByForumId(id);
		return ResultUtils.resultSucceed(result);
	}
	
	/**
	 * 创建新帖子
	 * @return
	 */
	@RequestMapping(value = "/add", method = RequestMethod.POST, produces = "application/json")
	public BaseBean<ForumBean> addForum(@RequestBody ForumBean forumBean) {
		forumBean.setDate(new Date().getTime()+"");
		forumDao.save(forumBean);
		return ResultUtils.resultSucceed("发布成功");
	}
	
	/**
	 * 回复
	 * @return
	 */
	@RequestMapping(value = "/", method = RequestMethod.POST, produces = "application/json")
	public BaseBean<ReplyBean> postReply(@RequestBody ReplyBean replyBean) {
		replyDao.save(replyBean);
		return ResultUtils.resultSucceed("回复成功");
	}
}

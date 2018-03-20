package com.example.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.bean.AccountBean;
import com.example.bean.BaseBean;
import com.example.bean.UserBean;
import com.example.dao.AccountDao;
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
	private AccountDao accountDao;

	@ApiOperation(value = "注册新业主", notes = "")
	@ApiImplicitParam(name = "accountBean", value = "业主账户信息对象AccountBean", required = true, dataType = "AccountBean")

	@RequestMapping(value = "/", method = RequestMethod.POST, produces = "application/json")
	public BaseBean<UserBean> addUser(@RequestBody AccountBean accountBean) {
		if (accountDao.findAccountByNumber(accountBean.getNumber()) == null) {
			AccountBean save = accountDao.save(accountBean);
			UserBean userBean = new UserBean();
			userBean.setNumber(save.getNumber());
			// 默认性别未知
			userBean.setSex(2);
			return ResultUtils.resultSucceed(userDao.save(userBean));
		} else {
			return ResultUtils.resultError("该账号已存在！");
		}
	}

	@ApiOperation(value = "业主登陆", notes = "")
	@ApiImplicitParam(name = "accountBean", value = "业主账户信息对象AccountBean", required = true, dataType = "AccountBean")

	@RequestMapping(value = "/login", method = RequestMethod.POST, produces = "application/json")
	public BaseBean<UserBean> userLogin(@RequestBody AccountBean accountBean) {
		AccountBean accout = accountDao.findAccountByNumber(accountBean.getNumber());
		if (accout != null) {
			if (accout.getPwd().equals(accountBean.getPwd())) {
				UserBean userBean = userDao.findUserByNumber(accountBean.getNumber());
				return ResultUtils.resultSucceed(userBean);
			}else {
				return ResultUtils.resultError("密码错误！");
			}
		} else {
			return ResultUtils.resultError("账号不存在或尚未注册！");
		}
	}

	/**
	 * 修改密码
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/editPass", method = RequestMethod.POST)
	public BaseBean<AccountBean> editPass(HttpServletRequest request) {
		String oldPass = request.getParameter("oldPass");
		String newPass = request.getParameter("newPass");
		String number = request.getParameter("number");
		AccountBean account = accountDao.findAccountByNumber(number);
		if (account.getPwd().equals(oldPass)) {
			account.setPwd(newPass);
			accountDao.save(account);
			return ResultUtils.resultSucceed("密码修改成功！");
		} else {
			return ResultUtils.resultError("旧密码有误！");
		}

	}

	@ApiOperation(value = "修改用户个人信息", notes = "")
	@ApiImplicitParam(name = "userBean", value = "业主用户信息对象UserBean", required = true, dataType = "UserBean")

	@RequestMapping(value = "/editUser", method = RequestMethod.POST, produces = "application/json")
	public BaseBean<UserBean> editUser(@RequestBody UserBean userBean) {
		UserBean user = userDao.findUserByNumber(userBean.getNumber());
		if (user!=null) {
			user.setName(userBean.getName());
			user.setTel(userBean.getTel());
			user.setSex(userBean.getSex());
			user.setDong(userBean.getDong());
			user.setDan(userBean.getDan());
			user.setHao(userBean.getHao());
			return ResultUtils.resultSucceed(userDao.save(user));
		}else {
			return ResultUtils.resultError("业主不存在！");
		}
		
	
	}

	/**
	 * 获取用户信息
	 * 
	 * @param number
	 * @return
	 */
	@RequestMapping(value = "/{number}", method = RequestMethod.GET)
	public BaseBean<UserBean> getUser(@PathVariable String number) {
		UserBean user = userDao.findUserByNumber(number);
		return ResultUtils.resultSucceed(user);
	}

	/**
	 * 用户充值
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/addMoney/{money}/{number}", method = RequestMethod.PUT)
	public BaseBean<AccountBean> addMoney(@PathVariable String number,@PathVariable String money) {
		Double m = Double.parseDouble(money);
		String n= number;
		UserBean user = userDao.findUserByNumber(n);
		user.setBalance(user.getBalance() + m);
		userDao.save(user);
		return ResultUtils.resultSucceed("充值成功！当前余额为：" + user.getBalance() + "元。");
	}

}

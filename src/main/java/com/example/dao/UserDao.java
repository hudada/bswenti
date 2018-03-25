package com.example.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.bean.QuestionBean;
import com.example.bean.UserBean;

public interface UserDao extends JpaRepository<UserBean, Long> {
	@Query("from UserBean b where b.userName=:userName and "
			+ "b.pwd=:pwd")
	UserBean findByNameAndPwd(@Param("userName") String userName,
			@Param("pwd") String pwd);
	
	@Query("from UserBean b where b.userName=:userName")
	UserBean findByName(@Param("userName") String userName);
	
	@Query("from UserBean b order by b.lid desc")
	List<UserBean> findOrderLid();
}

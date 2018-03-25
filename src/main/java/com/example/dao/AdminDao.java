package com.example.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.bean.AdminBean;
import com.example.bean.UserBean;

public interface AdminDao extends JpaRepository<AdminBean, Long> {
	@Query("from AdminBean b where b.userName=:userName and "
			+ "b.pwd=:pwd")
	AdminBean findByNameAndPwd(@Param("userName") String userName,
			@Param("pwd") String pwd);
}

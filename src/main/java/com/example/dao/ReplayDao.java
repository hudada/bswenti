package com.example.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.bean.AdminBean;
import com.example.bean.ReplayBean;
import com.example.bean.UserBean;

public interface ReplayDao extends JpaRepository<ReplayBean, Long> {
	@Query("from ReplayBean b where b.qid=:qid")
	List<ReplayBean> findByQid(@Param("qid") Long qid);
}

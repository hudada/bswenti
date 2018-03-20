package com.example.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.bean.AccountBean;
import com.example.bean.NoticeBean;
import com.example.bean.UserBean;

public interface NoticeDao extends JpaRepository<NoticeBean, Long> {

}

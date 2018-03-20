package com.example.dao;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.bean.ForumBean;

public interface ForumDao extends JpaRepository<ForumBean, Long> {
}

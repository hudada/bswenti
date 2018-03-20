package com.example.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.bean.AccountBean;
import com.example.bean.ForumBean;
import com.example.bean.ReplyBean;

public interface ReplyDao extends JpaRepository<ReplyBean, Long> {
	@Query(value="SELECT COUNT(*) FROM reply_bean AS r WHERE r.`forum_id` = ?1",nativeQuery=true)
    int findCountByForumId(String id);
	
	@Query(value="SELECT * FROM reply_bean AS r WHERE r.`forum_id` = ?1",nativeQuery=true)
    List<ReplyBean> findReplayByForumId(String id);
	
	@Modifying
	@Query(value="DELETE FROM reply_bean WHERE `forum_id` = ?1",nativeQuery=true)
    void deleteByForumId(String id);
}

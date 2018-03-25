package com.example.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.bean.QuestionBean;
import com.example.bean.UserBean;

public interface QuestionDao extends JpaRepository<QuestionBean, Long> {
	@Query("from QuestionBean b where b.uid=:uid and "
			+ "b.info like %:info% "
			+ "order by b.status,b.time desc")
	List<QuestionBean> findByKeyOrderStatus(@Param("uid") Long uid,
			@Param("info") String info);
	
	@Query("from QuestionBean b where b.uid=:uid and "
			+ "b.info like %:info% and "
			+ "b.status=:status "
			+ "order by b.time desc")
	List<QuestionBean> findByKeyOrderStatus(@Param("uid") Long uid,
			@Param("status") int status,
			@Param("info") String info);
	
	@Query(value="SELECT SUM(b.score) FROM question_bean AS b WHERE b.`team` = ?1 and b.`status`=2"
			,nativeQuery=true)
	double findScoreByTeam(int team);
	
	@Query(value="SELECT COUNT(b.id) FROM question_bean AS b WHERE b.`status` = ?1"
			,nativeQuery=true)
	int findByStatus(int status);
	
	@Query("from QuestionBean b where b.status=:status and b.type=:type")
	List<QuestionBean> findListByStatusAndType(@Param("status") int status,
			@Param("type") int type);
	
	@Query("from QuestionBean b where b.status=:status")
	List<QuestionBean> findListByStatusAndType(@Param("status") int status);
	
	@Query("from QuestionBean b where b.team=:team and "
			+ "b.info like %:info% and "
			+ "b.status=:status "
			+ "order by b.time desc")
	List<QuestionBean> findListByTean(@Param("team") int team,
			@Param("status") int status,
			@Param("info") String info);
	
	@Query("from QuestionBean b where b.team=:team and "
			+ "b.info like %:info% "
			+ "order by b.status,b.time desc")
	List<QuestionBean> findListByTean(@Param("team") int team,
			@Param("info") String info);
	
	@Query("from QuestionBean b where b.team=:team and "
			+ "b.status!=2 "
			+ "order by b.status, b.time desc")
	List<QuestionBean> findListByTean(@Param("team") int team);
}

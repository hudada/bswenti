package com.example.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.bean.PamentRecordBean;


public interface pamentRecordDao extends JpaRepository<PamentRecordBean, Long> {
	@Query("from PamentRecordBean p where p.number=:number")
    List<PamentRecordBean> findAccountByNumber(@Param("number") String number);
	
	@Query("from PamentRecordBean p where p.type=:type")
    List<PamentRecordBean> findAccountByType(@Param("type") Integer type);
	
	@Query("from PamentRecordBean p where p.state=:state")
    List<PamentRecordBean> findAccountByState(@Param("state") Integer state);
	
	@Query("from PamentRecordBean p order by p.state")
    List<PamentRecordBean> findOrderByState();
}

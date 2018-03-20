package com.example.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.bean.AccountBean;
import com.example.bean.UserBean;

public interface AccountDao extends JpaRepository<AccountBean, Long> {
	@Query("from AccountBean b where b.number=:number")
    AccountBean findAccountByNumber(@Param("number") String number);
}

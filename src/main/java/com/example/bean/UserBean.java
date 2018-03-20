package com.example.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
//用户信息表
@Entity
public class UserBean {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	// 账号
	@Column(unique = true)
	private String number;
	private String name;
	private String tel;
	private int sex;
	private String Dong;
	private String Dan;
	private String Hao;
	public String getDong() {
		return Dong;
	}

	public void setDong(String dong) {
		Dong = dong;
	}

	public String getDan() {
		return Dan;
	}

	public void setDan(String dan) {
		Dan = dan;
	}

	public String getHao() {
		return Hao;
	}

	public void setHao(String hao) {
		Hao = hao;
	}

	// 钱包余额
	private double balance;

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

}

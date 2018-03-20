package com.example.bean;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class ForumAndReplyCount extends ForumBean{

	private int count;


	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
	
}

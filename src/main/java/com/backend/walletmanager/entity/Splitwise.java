package com.backend.walletmanager.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "splitwise")
public class Splitwise {

	@Id
	@Column(name = "user_id")
    private Integer userId;
	
    @Column(name = "splitwise_key")
    private String splitwise_key;
	
	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getSplitwise_key() {
		return splitwise_key;
	}

	public void setSplitwise_key(String splitwise_key) {
		this.splitwise_key = splitwise_key;
	}

	@Override
	public String toString() {
		return "Splitwise [userId=" + userId + ", splitwise_key=" + splitwise_key + "]";
	}
    
    

}

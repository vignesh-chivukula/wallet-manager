package com.backend.walletmanager.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.plaid.client.model.AccountType;

@Entity
@Table(name = "external_accounts")
public class ExternalAccounts {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "external_account_id")
	private Integer externalAccountId;
	
	@Column(name = "user_id")
    private Integer userId;
	
	@Column(name="account_id")
	private String accountId;
	
	@Column(name="account_name")
	private String accountName;
	
	@Column(name="account_type")
	private AccountType accountType;
	
	@Column(name="institution_name")
	private String institutionName;
	
	@Column(name="institution_id")
	private String institutionId;
	
	@Column(name="access_key")
	private String accessKey;
	
	@Column(name="is_active")
	private Boolean isActive;

	public ExternalAccounts() {
		super();
	}




	public ExternalAccounts(Integer externalAccountId, Integer userId, String accountId, String accountName,
			AccountType accountType, String institutionName, String institutionId, String accessKey, Boolean isActive) {
		super();
		this.externalAccountId = externalAccountId;
		this.userId = userId;
		this.accountId = accountId;
		this.accountName = accountName;
		this.accountType = accountType;
		this.institutionName = institutionName;
		this.institutionId = institutionId;
		this.accessKey = accessKey;
		this.isActive = isActive;
	}

	


	public Integer getExternalAccountId() {
		return externalAccountId;
	}




	public void setExternalAccountId(Integer externalAccountId) {
		this.externalAccountId = externalAccountId;
	}




	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public AccountType getAccountType() {
		return accountType;
	}

	public void setAccountType(AccountType accountType) {
		this.accountType = accountType;
	}

	

	public String getInstitutionName() {
		return institutionName;
	}



	public void setInstitutionName(String institutionName) {
		this.institutionName = institutionName;
	}



	public String getInstitutionId() {
		return institutionId;
	}



	public void setInstitutionId(String institutionId) {
		this.institutionId = institutionId;
	}



	public String getAccessKey() {
		return accessKey;
	}

	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}




	@Override
	public String toString() {
		return "ExternalAccounts [externalAccountId=" + externalAccountId + ", userId=" + userId + ", accountId="
				+ accountId + ", accountName=" + accountName + ", accountType=" + accountType + ", institutionName="
				+ institutionName + ", institutionId=" + institutionId + ", accessKey=" + accessKey + ", isActive="
				+ isActive + "]";
	}



	
	
	
	

}

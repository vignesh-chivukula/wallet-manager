package com.backend.walletmanager.Model;

public class PublicTokenRequest {
	 private String publicToken;
     private Integer userId;
     public String getPublicToken() { return publicToken; }
     public Integer getUserId() {return userId;};
     public void setPublicToken(String publicToken) { this.publicToken = publicToken; }
     public void setuserId(Integer userId) {this.userId = userId;}
}

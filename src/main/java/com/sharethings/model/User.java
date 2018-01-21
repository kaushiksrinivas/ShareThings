package com.sharethings.model;

public class User {
	String UserName;
	String Mobile;
	String Password;
	String UserType;
	int OrderCount = 0;
	
	public User(){
		
	}
	
	public User(String Mobile, String Password){
		this.Mobile = Mobile;
		this.Password = Password;
	}
	
	public String getUserName() {
		return UserName;
	}
	public void setUserName(String userName) {
		UserName = userName;
	}
	public String getMobile() {
		return Mobile;
	}
	public void setMobile(String mobile) {
		Mobile = mobile;
	}
	public String getPassword() {
		return Password;
	}
	public void setPassword(String password) {
		Password = password;
	}
	public String getUserType() {
		return UserType;
	}
	public void setUserType(String userType) {
		UserType = userType;
	}
	public int getOrderCount() {
		return OrderCount;
	}
	public void setOrderCount(int orderCount) {
		OrderCount = orderCount;
	}
	
	

}

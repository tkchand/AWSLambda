package com.poc.lambda.pojo;

public class UserBean {

	private String userName;
	private Integer Id;
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public Integer getId() {
		return Id;
	}
	public void setId(Integer id) {
		Id = id;
	}
	
	@Override
	public String toString() {		 
		return "UserName:"+userName+" Id:"+Id;
	}
}

package com.poc.lambda.pojo;

public class ErrorMgmt {

	private String errorCode;
	private String errorMessage;
	public ErrorMgmt() {
		 
	}
	public ErrorMgmt(String errorCode, String errorMessage) {
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}
	
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	@Override
	public String toString() {		 
		return "Error Code:"+errorCode+"  Error Message:"+errorMessage;
	}
}

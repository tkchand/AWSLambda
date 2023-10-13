package com.poc.lambda.pojo;

public class Response<T> {

	private int httpStatusCode;
	private T responseBody;
	private ErrorMgmt error;

	public Response(int httpStatusCode, T responseBody, ErrorMgmt error) {
		this.httpStatusCode = httpStatusCode;
		this.responseBody = responseBody;
		this.error = error;
	}

	public int getHttpStatusCode() {
		return httpStatusCode;
	}
	public void setHttpStatusCode(int httpStatusCode) {
		this.httpStatusCode = httpStatusCode;
	}
	public T getResponseBody() {
		return responseBody;
	}
	public void setResponseBody(T responseBody) {
		this.responseBody = responseBody;
	}
	public ErrorMgmt getError() {
		return error;
	}
	public void setError(ErrorMgmt error) {
		this.error = error;
	}

	@java.lang.Override
	public java.lang.String toString() {
		return "Response{" +
				"httpStatusCode=" + httpStatusCode +
				", responseBody=" + responseBody +
				", error=" + error +
				'}';
	}
}

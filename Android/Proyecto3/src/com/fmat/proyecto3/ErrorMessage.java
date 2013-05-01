package com.fmat.proyecto3;

public class ErrorMessage {

	private String message;
	private String type;
	private int code;

	public int getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

	public String getType() {
		return type;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setType(String type) {
		this.type = type;
	}

}

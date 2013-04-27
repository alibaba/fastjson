package com.derbysoft.spitfire.fastjson.dto;

public class WarningDTO extends  AbstractDTO{
    private String code;
	private String message;

	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
}

package com.gdu.ocean.exception;

import lombok.Getter;

public enum ExceptionCode {
	MEMBER_NOT_FOUND(404, "Member Not Found")
	, PAY_CANCEL(404, "Pay Cancel")
	, PAY_FAILED(404, "Pay Failed");
	
	@Getter
	private int status;
	
	@Getter
	private String message;
	
	ExceptionCode(int status, String message) {
		this.status = status;
		this.message = message;
	}
}

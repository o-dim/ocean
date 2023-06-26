package com.gdu.ocean.service;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

public interface IamPortService {

	String getToken() throws IOException;

	int paymentInfo(HttpServletRequest request, String imp_uid, String access_token) throws IOException;

	void payMentCancel(String access_token, String imp_uid, String amount, String reason) throws IOException;
	
}

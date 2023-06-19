package com.gdu.ocean.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gdu.ocean.domain.UsersDTO;

public interface UsersService {
	
	public Map<String, Object> verifyEmail(String email);
	public Map<String, Object> verifyPhoneNo(String phoneNo);
	public Map<String, Object> sendAuthCode(String email); 
	public void join(HttpServletRequest request, HttpServletResponse response);
	public void login(HttpServletRequest request, HttpServletResponse response);
	public void autologin(HttpServletRequest request, HttpServletResponse response);
	public void logout(HttpServletRequest request, HttpServletResponse response);
	public void out(HttpServletRequest request, HttpServletResponse response);
	public void sleepUsersHandle();
	public void restore(HttpServletRequest request, HttpServletResponse response);
	public boolean checkPw(HttpServletRequest request);
	public Map<String, Object> modifyPw(HttpServletRequest request);
	public UsersDTO getUserByEmail(String email);
	public Map<String, Object> modifyInfo(HttpServletRequest request);
	public Map<String, Object> findEmail(UsersDTO usersDTO);
	public Map<String, Object> findPw(UsersDTO usersDTO);
	public Map<String, Object> sendTempPw(UsersDTO usersDTO);
}

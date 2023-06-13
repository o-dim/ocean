package com.gdu.ocean.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;


public interface ShopService {

	public void newList(HttpServletRequest request, Model model);
	public void getCdByNo(int cdNo, Model model);
	//public void getCartCount(int cdNo);

}

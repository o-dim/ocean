package com.gdu.ocean.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;

import com.gdu.ocean.domain.CartDetailDTO;


public interface ShopService {

	public void getCdList(HttpServletRequest request, Model model);
	public void getCdByNo(int cdNo, Model model);
	public void getcartList(HttpServletRequest request, Model model);
	public void getCartListFK(int cartNo, int cdNo, Model model);

}

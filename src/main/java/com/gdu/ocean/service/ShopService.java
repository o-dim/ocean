package com.gdu.ocean.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;

import com.gdu.ocean.domain.CartDTO;
import com.gdu.ocean.domain.CartDetailDTO;
import com.gdu.ocean.domain.HashtagDTO;


public interface ShopService {

	public void getCdList(HttpServletRequest request, Model model);
	public void getCdByNo(int cdNo, Model model);
	public List<HashtagDTO> getHashtagName(int cdNo);
	public List<CartDTO> getCartNo(int cartNo);
	//public List<CartDetailDTO> getCartDetailNo(int cartDetailNo);
	
	//public void getcartList(HttpServletRequest request, Model model);
	//public void getCartListFK(int cartNo, int cdNo, Model model);
	
}

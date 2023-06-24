package com.gdu.ocean.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;

import com.gdu.ocean.domain.HashtagDTO;


public interface ShopService {

	public void getCdList(HttpServletRequest request, Model model);
	public void getCdByNo(int cdNo, Model model);
	public List<HashtagDTO> getHashtagName(int cdNo);
	
	public Map<String, Object> addCartDetail(HttpServletRequest request);
	public void getCartDetailList(int cartNo, Model model);
	
	
	
}

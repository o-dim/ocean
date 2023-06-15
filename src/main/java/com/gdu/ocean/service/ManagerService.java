package com.gdu.ocean.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;

import com.gdu.ocean.domain.HashtagDTO;

public interface ManagerService {
	public void getSaleList(HttpServletRequest request, Model model);
	public Map<String, Object> getHashtagByNo(String cdNo);
	public int removeCd(int cdNo);
	public List<HashtagDTO> getHashtagList();
	public void getUserListPagination(HttpServletRequest request, Model model);
	
	/*
	 * public void addCd(HttpServletRequest request, HttpServletResponse response);
	 */
}

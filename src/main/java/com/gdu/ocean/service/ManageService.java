package com.gdu.ocean.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.gdu.ocean.domain.HashtagDTO;

public interface ManageService {
	public void getSaleList(HttpServletRequest request, Model model);
	public Map<String, Object> getHashtagByNo(String cdNo);
	public int removeCd(int cdNo);
	public List<HashtagDTO> getHashtagList();
	public void getUserList(HttpServletRequest request, Model model);
	public void getSleepUserList(HttpServletRequest request, Model model);
	public void getOutUserList(HttpServletRequest request, Model model);
	public int userout(String email, HttpServletRequest request, HttpServletResponse response);
	public void getBoardList(HttpServletRequest request, Model model);
	public int removeReply(int replyNo);
	public int addCd(MultipartHttpServletRequest multipartRequest) throws Exception;
	public ResponseEntity<byte[]> display(int cdNo);
	public ResponseEntity<byte[]> displaydetail(int cdNo);
	public void getOrderList(HttpServletRequest request, Model model);
}

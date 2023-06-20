package com.gdu.ocean.service;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.gdu.ocean.domain.ReplyDTO;
import com.gdu.ocean.domain.UsersDTO;
import com.gdu.ocean.mapper.ReplyMapper;
import com.gdu.ocean.util.PageUtil;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ReplyServiceImpl implements ReplyService {
	private final ReplyMapper replyMapper;
	private final PageUtil pageUtil;
	
	@Override
	public Map<String, Object> addComment(HttpServletRequest request){
		String content = request.getParameter("content");
		int userNo = Integer.parseInt(request.getParameter("userNo"));
		
		UsersDTO userDTO = new UsersDTO();
		userDTO.setUserNo(userNo);
		ReplyDTO replyDTO = new ReplyDTO();
		replyDTO.setContent(content);
		replyDTO.setUsersDTO(userDTO);
		
		Map<String, Object> map = new HashMap<>();
		map.put("isAdd", replyMapper.addComment(replyDTO));
		
		return map;
	}
	
	@Override
	public Map<String, Object> getCommentCount(int idolNo){
		return null;
	}
	
	@Override
	public Map<String, Object> getCommentList(HttpServletRequest request){
		int idolNo = Integer.parseInt(request.getParameter("idolNo"));
		int page = Integer.parseInt(request.getParameter("page"));
		int commentCount = replyMapper.getCommentCount(idolNo);
		int recordPerPage = 1;
		
		pageUtil.setPageUtil(page, commentCount, recordPerPage);
		
		Map<String, Object> map = new HashMap<>();
		map.put("idolNo", idolNo);
		map.put("begin", pageUtil.getBegin());
		map.put("end", pageUtil.getEndPage());
		
		Map<String, Object> result = new HashMap<>();
		result.put("commentList", replyMapper.getCommentList(map));
		result.put("pageUtil", pageUtil);
		
		return result;
	}
	
	@Override
	public Map<String, Object> addReply(HttpServletRequest request){
		String content = request.getParameter("content");
		int idolNo = Integer.parseInt(request.getParameter("idolNo"));
		int groupNo = Integer.parseInt(request.getParameter("groupNo"));
		int userNo = Integer.parseInt(request.getParameter("userNo"));
		
		UsersDTO usersDTO = new UsersDTO();
		usersDTO.setUserNo(userNo);
		
		ReplyDTO replyDTO = new ReplyDTO();
		replyDTO.setContent(content);
		replyDTO.setGroupNo(groupNo);
		replyDTO.setUsersDTO(usersDTO);
		
		Map<String, Object> map = new HashMap<>();
		map.put("isAdd", replyMapper.addReply(replyDTO) == 1);
		
		return map;
	}
	
	@Override
	public Map<String, Object> removeReply(int replyNo) {
		Map<String, Object> map = new HashMap<>();
		map.put("isRemove", replyMapper.deleteComment(replyNo));
		
		return map;
		
	}
}

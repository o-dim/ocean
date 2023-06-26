package com.gdu.ocean.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.gdu.ocean.domain.ReplyDTO;
import com.gdu.ocean.domain.UsersDTO;
import com.gdu.ocean.mapper.ReplyMapper;
import com.gdu.ocean.util.PageUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
@Slf4j
public class ReplyServiceImpl implements ReplyService {
	private final ReplyMapper replyMapper;
	private final PageUtil pageUtil;
	
	@Override
	public Map<String, Object> addComment(HttpServletRequest request){
		log.info("addComment도착.............");
		String content = request.getParameter("content");
		log.info("content도착 : " + content );
		int idolNo = Integer.parseInt(request.getParameter("idolNo"));
		log.info(idolNo + " : idolNo 도착.............");
		int userNo = Integer.parseInt(request.getParameter("userNo"));
		log.info(userNo + " : userNo 도착.............");
		
		UsersDTO userDTO = new UsersDTO();
		userDTO.setUserNo(userNo);
		ReplyDTO replyDTO = new ReplyDTO();
		replyDTO.setContent(content);
		replyDTO.setUsersDTO(userDTO);
		replyDTO.setIdolNo(idolNo);
		
		Map<String, Object> map = new HashMap<>();
		map.put("isAdd", replyMapper.addComment(replyDTO) == 1);
		
		return map;
	}
	
	@Override
	public Map<String, Object> getCommentCount(int page) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Map<String, Object> getCommentList(HttpServletRequest request){
		int idolNo = Integer.parseInt(request.getParameter("idolNo"));
		int page = Integer.parseInt(request.getParameter("page"));
		int commentCount = replyMapper.getCommentCount(idolNo);
		int recordPerPage = 5;
		
		pageUtil.setPageUtil(page, commentCount, recordPerPage);
		
		Map<String, Object> map = new HashMap<>();
		map.put("idolNo", idolNo);
		map.put("begin", pageUtil.getBegin());
		map.put("recordPerPage", recordPerPage);
		List<ReplyDTO> commentList = replyMapper.getCommentList(map);
		Map<String, Object> result = new HashMap<>();
		result.put("commentList", replyMapper.getCommentList(map));
		result.put("pageUtil", pageUtil.getPagination("/shop/aespa/pagination.do"));
		
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

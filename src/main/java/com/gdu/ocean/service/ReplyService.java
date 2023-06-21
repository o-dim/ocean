package com.gdu.ocean.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;

import com.gdu.ocean.domain.ReplyDTO;

public interface ReplyService {

	public Map<String, Object> getCommentCount(int page);
	public Map<String, Object> addComment(HttpServletRequest request);
	public Map<String, Object> addReply(HttpServletRequest request);
	public Map<String, Object> removeReply(int replyNo);
	public Map<String, Object> getCommentList(HttpServletRequest request);
}

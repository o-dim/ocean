package com.gdu.ocean.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public interface ReplyService {

	public Map<String, Object> getCommentList(HttpServletRequest request);
	public Map<String, Object> getCommentCount(int page);
	public Map<String, Object> addComment(HttpServletRequest request);
	public Map<String, Object> addReply(HttpServletRequest request);
	public Map<String, Object> removeReply(int replyNo);
}

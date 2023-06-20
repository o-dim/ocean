package com.gdu.ocean.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gdu.ocean.service.ReplyService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {
	
	private final ReplyService replyService;
	
	@GetMapping(value = "/list.do", produces = "application/json")
	@ResponseBody
	public Map<String, Object> getCommentList(HttpServletRequest request) {
		return replyService.getCommentList(request);
	}
	
	@PostMapping(value = "/addComment.do", produces = "application/json")
	@ResponseBody
	public Map<String, Object> addComment(HttpServletRequest request) {
		return replyService.addComment(request);
	}
	
	@PostMapping(value = "/addReply.do", produces = "application/json")
	@ResponseBody
	public Map<String, Object> addReply(HttpServletRequest request){
		return replyService.addReply(request);
	}
	
	@PostMapping("/remove.do")
	@ResponseBody
	public String removeReply(@RequestParam("replyNo") int replyNo) {
		replyService.removeReply(replyNo);
		return "redirect:/shop/idol.html";
	}
}

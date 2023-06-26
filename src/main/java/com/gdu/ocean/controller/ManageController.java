package com.gdu.ocean.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gdu.ocean.service.ManageService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/manage")
@Controller
public class ManageController {
	
	private final ManageService manageService;

	@GetMapping("/salelist.do")
	public String salelist(HttpServletRequest request, Model model) {
		manageService.getSaleList(request, model);
		return "manage/salelist";
	}
	
	@GetMapping(value="/getHashtag.do", produces="application/json")
	@ResponseBody
	public Map<String, Object> getHashtag(@RequestParam("cdNo") String cdNo) {
		return manageService.getHashtagByNo(cdNo);
	}
	
	@PostMapping("/remove.do")
	public String remove(int cdNo, RedirectAttributes redirectAttributes) {
		int removeResult = manageService.removeCd(cdNo);
		redirectAttributes.addFlashAttribute("removeResult", removeResult);
		return "redirect:/manage/salelist.do";
	}
	
	
	
	@GetMapping("/sale.html")
	public String sale(HttpServletRequest request, Model model) {
		manageService.getOrderList(request, model);
		return "manage/sale";
	}
	

	@GetMapping("/board.do")
	public String board(HttpServletRequest request, Model model) {
		manageService.getBoardList(request, model);
		return "manage/board.html";
	}
	
	@PostMapping("/boardremove.do")
	public String boardremove(int replyNo, RedirectAttributes redirectAttributes) {
		int removeResult = manageService.removeReply(replyNo);
		redirectAttributes.addFlashAttribute("removeResult", removeResult);
		return "redirect:/manage/board.do";
	}
	
	// 해시태그 목록 가져오기
	@GetMapping("/cdadd.html")
	public String cdaddHtml(Model model) {
		model.addAttribute("hashtagList", manageService.getHashtagList());
		return "manage/cdadd";
	}
	
	// 페이지 네이션 목록가져오기
	@GetMapping("/membersearch.do")
	public String pagination(HttpServletRequest request, Model model) {
		manageService.getUserList(request, model);
		return "manage/member";
	}
	
	@GetMapping("/sleepmember.do")
	public String sleepmember(HttpServletRequest request, Model model) {
		manageService.getSleepUserList(request, model);
		return "manage/sleepmember";
	}
	
	@GetMapping("/outmember.do")
	public String outmember(HttpServletRequest request, Model model) {
		manageService.getOutUserList(request, model);
		return "manage/outmember";
	}
	
	@ResponseBody
	@GetMapping("/userout.do")
	public int userout(String email, HttpServletRequest request, HttpServletResponse response) {
		int result = manageService.userout(email, request, response);
		return result;
	}
	
	// 상품 추가하기
   @PostMapping("/addCd.do")
   public String addCd(MultipartHttpServletRequest multipartRequest, RedirectAttributes redirectAttributes) throws Exception {
      manageService.addCd(multipartRequest);
       return "redirect:/manage/salelist.do";
   }
   
   @GetMapping("/display.do")
   public ResponseEntity<byte[]> display(@RequestParam("cdNo") int cdNo) {
      return manageService.display(cdNo);
   }
   
   @GetMapping("/displaydetail.do")
   public ResponseEntity<byte[]> displaydetail(@RequestParam("cdNo") int cdNo) {
	   return manageService.displaydetail(cdNo);
   }
   
	
}

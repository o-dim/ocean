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

import com.gdu.ocean.service.ManagerService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/manager")
@Controller
public class ManagerController {
	
	private final ManagerService managerService;

	@GetMapping("/salelist.do")
	public String salelist(HttpServletRequest request, Model model) {
		managerService.getSaleList(request, model);
		return "manager/salelist";
	}
	
	@GetMapping(value="/getHashtag.do", produces="application/json")
	@ResponseBody
	public Map<String, Object> getHashtag(@RequestParam("cdNo") String cdNo) {
		return managerService.getHashtagByNo(cdNo);
	}
	
	@PostMapping("/remove.do")
	public String remove(int cdNo, RedirectAttributes redirectAttributes) {
		int removeResult = managerService.removeCd(cdNo);
		redirectAttributes.addFlashAttribute("removeResult", removeResult);
		return "redirect:/manager/salelist.do";
	}
	
	
	
	@GetMapping("/sale.html")
	public String sale() {
		return "manager/sale";
	}
	

	@GetMapping("/board.do")
	public String board(HttpServletRequest request, Model model) {
		managerService.getBoardList(request, model);
		return "manager/board.html";
	}
	
	@PostMapping("/boardremove.do")
	public String boardremove(int replyNo, RedirectAttributes redirectAttributes) {
		int removeResult = managerService.removeReply(replyNo);
		redirectAttributes.addFlashAttribute("removeResult", removeResult);
		return "redirect:/manager/board.do";
	}
	
	// 해시태그 목록 가져오기
	@GetMapping("/cdadd.html")
	public String cdaddHtml(Model model) {
		model.addAttribute("hashtagList", managerService.getHashtagList());
		return "manager/cdadd";
	}
	
	// 페이지 네이션 목록가져오기
	@GetMapping("/membersearch.do")
	public String pagination(HttpServletRequest request, Model model) {
		managerService.getUserList(request, model);
		return "manager/member";
	}
	
	@GetMapping("/sleepmember.do")
	public String sleepmember(HttpServletRequest request, Model model) {
		managerService.getSleepUserList(request, model);
		return "manager/sleepmember";
	}
	
	@GetMapping("/outmember.do")
	public String outmember(HttpServletRequest request, Model model) {
		managerService.getOutUserList(request, model);
		return "manager/outmember";
	}
	
	@GetMapping("/userout.do")
	public void userout(HttpServletRequest request, HttpServletResponse response) {
		managerService.userout(request, response);
	}
	
	// 상품 추가하기
   @PostMapping("/addCd.do")
   public String addCd(MultipartHttpServletRequest multipartRequest, RedirectAttributes redirectAttributes) throws Exception {
      managerService.addCd(multipartRequest);
       return "redirect:/manager/salelist.do";
   }
   
   @GetMapping("/display.do")
   public ResponseEntity<byte[]> display(@RequestParam("cdNo") int cdNo) {
      return managerService.display(cdNo);
   }
   
	
}

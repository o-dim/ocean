package com.gdu.ocean.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
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
	

	@GetMapping("/board.html")
	public String board() {
		return "manager/board";
	}
	
	// 해시태그 목록 가져오기
	@GetMapping("/cdadd.html")
	public String cdaddHtml(Model model) {
		model.addAttribute("hashtagList", managerService.getHashtagList());
		return "manager/cdadd";
	}
	
	/*
	 * @PostMapping("/add.do") public void add(HttpServletRequest request,
	 * HttpServletResponse response) { managerService.addCd(request, response); }
	 */
	
	// 페이지 네이션 목록가져오기
	@GetMapping("/member.html")
	public String pagination(HttpServletRequest request, Model model) {
		managerService.getUserListPagination(request, model);
		return "manager/member";
	}
	
	@GetMapping(value="/getHashtag.do", produces="application/json")
	@ResponseBody
	public Map<String, Object> getHashtag(@RequestParam("cdNo") String cdNo) {
		System.out.println(managerService.getHashtagByNo(cdNo));
		return managerService.getHashtagByNo(cdNo);
	}
	
	

	
	
}

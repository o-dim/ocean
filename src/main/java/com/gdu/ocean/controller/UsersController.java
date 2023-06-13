package com.gdu.ocean.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gdu.ocean.domain.UsersDTO;
import com.gdu.ocean.service.UsersService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/users")
@Controller
public class UsersController {
	
	//field
	private final UsersService usersService;
	
	@GetMapping("/agree.html")
	public String agreeForm() {
		return "users/agree";
	}
	
	@GetMapping("/join.html")
	public String joinForm(@RequestParam(value="location", required=false) String location
						  ,@RequestParam(value="event", required=false) String event
						  , Model model) {
	  model.addAttribute("location", location);
	  model.addAttribute("event", event);
	  return "users/join";
	}
	
	@ResponseBody
	@GetMapping(value="/verifyEmail.do", produces="application/json")
	public Map<String, Object> verifyEmail(@RequestParam("email") String email) {
		return usersService.verifyEmail(email);
	}
	
	@ResponseBody
	@GetMapping(value="/sendAuthCode.do", produces="application/json")
	public Map<String, Object> sendAuthCode(@RequestParam("email") String email) {
		return usersService.sendAuthCode(email);
	}
	
	@PostMapping("/join.do")
	public void join(HttpServletRequest reqeust, HttpServletResponse response) {
		usersService.join(reqeust, response);
	}
	
	@GetMapping("/login.html")
	public String loginForm(HttpServletRequest request, Model model) {
		// 요청 헤더 referer : 로그인 화면으로 이동하기 직전의 주소를 저장하는 헤더 값
		String url = request.getHeader("referer");
	    model.addAttribute("url", url == null ? request.getContextPath() : url);
		return "users/login";
	}
	
	@PostMapping("/login.do")
	public void login(HttpServletRequest request, HttpServletResponse response) {
		usersService.login(request, response);
	}
	
	@GetMapping("/logout.do") 
	public String logout(HttpServletRequest request, HttpServletResponse response) {
		usersService.logout(request, response);
		return "redirect:/";
	}
	
	@GetMapping("/out.do")
	public void out(HttpServletRequest request, HttpServletResponse response) {
		usersService.out(request, response);
	}
	
	@GetMapping("/restore.do") // 휴면 복원 
	public void restore(HttpServletRequest request, HttpServletResponse response) {
		usersService.restore(request, response);
	}
	
	@GetMapping("/checkPw.form") // 마이페이지 직전 비밀번호 확인 화면으로 이동 
	public String checkPwForm() {
		return "users/checkPw";
	}
	
	@ResponseBody
	@PostMapping(value="/checkPw.do", produces="application/json") // 사용자가 입력한 비밀번호가 맞는지 확인
	public Map<String, Object> checkPw(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("isCorrect", usersService.checkPw(request));
		return map;
	}
	
	@GetMapping("/mypage.do") // 마이페이지로 이동 
	public String mypage(HttpSession session, Model model) {
		String email = (String)session.getAttribute("loginEmail");
		model.addAttribute("loginUsers", usersService.getUserByEmail(email));
		return "users/mypage";
	}
	
	@ResponseBody
	@PostMapping(value="/modiftPw.do", produces="application/json") // 비밀번호 변경 
	public Map<String, Object> modifyPw(HttpServletRequest request) {
		return usersService.modifyPw(request);
	}
	
	@GetMapping("/findEmail.html") // 이메일 찾기 화면으로 이동 
	public String findEmailForm() {
		return "users/findEmail";
	}
	
	  @ResponseBody
	  @PostMapping(value="/findEmail.do", produces="application/json")  // 아이디 찾기
	  public Map<String, Object> findEmail(@RequestBody UsersDTO usersDTO) {
	    return usersService.findEmail(usersDTO);
	  }
	  
	  @GetMapping("/findPw.html")  // 비밀번호 찾기 화면으로 이동
	  public String findPwForm() {
	    return "users/findPw";
	  }
	  
	  @ResponseBody
	  @PostMapping(value="/findPw.do", produces="application/json")  // 비밀번호 찾기
	  public Map<String, Object> findPw(@RequestBody UsersDTO usersDTO) {
	    return usersService.findPw(usersDTO);
	  }
	  
	  @ResponseBody
	  @PostMapping(value="/sendTempPw.do", produces="application/json")  // 임시비밀번호 발급 및 전송
	  public Map<String, Object> sendTempPw(@RequestBody UsersDTO usersDTO) {
	    return usersService.sendTempPw(usersDTO);
	  }
	  
}

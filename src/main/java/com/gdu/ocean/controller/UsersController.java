package com.gdu.ocean.controller;

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
	    // 요청 헤더 referer: 로그인 화면으로 이동하기 직전의 주소를 저장하는 헤더 값
	    String url = request.getHeader("referer");
	    model.addAttribute("url", url == null || url.isEmpty() ? "/index.html" : url);
	    
	    /*
	      네이버로그인 1단계
	      1. 네이버로그인 링크를 클릭하면 어디로 이동해야 하는지 네이버로부터 링크를 받아오는데 그 링크를 apiURL이라고 부른다. 받아 온 링크에는 네이버로그인 진행을 위한 각종 파라미터가 포함된다.
	      2. 1단계 흐름 : 앱 로그인 페이지 -> 네이버로그인 링크 클릭 -> 네이버 로그인 화면으로 이동 -> 네이버 아이디/비번 입력 후 로그인 -> 네이버로그인 후속 처리를 위해서 다시 앱으로 돌아옴
	      3. 1단계 흐름의 마지막 단계에서 되돌아 앱의 주소를 redirect_url이라고 부른다. redirect_url에는 네이버로그인을 계속해서 처리할 mapping을 지정해 준다.
	      4. 어떤 redirect_url을 사용할 것인지 getNaverLoginApiURL() 메소드에 작성한다.
	      5. 어떤 redirect_url을 사용할 것인지 네이버개발자센터 > 내 애플리케이션 > "네이버 로그인 Callback URL (최대 5개)"에도 동일한 주소를 작성한다.
	      6. 1단계에서 사용하는 코드는 네이버개발자센터 > Documents > 네이버 로그인 > API 명세 > 로그인 API 명세 > "naverlogin.jsp"에서 제공한다.
	    */
	    model.addAttribute("naverApiURL", usersService.getNaverLoginApiURL(request));
	    model.addAttribute("kakaoApiURL", usersService.getKakaoLoginApiURL(request));
	    
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
	
   @GetMapping("/wakeup.html")  // 휴면 복원 화면으로 이동
   public String wakeup() {
     return "users/wakeup";
   }

	@GetMapping("/restore.do") // 휴면 복원 
	public void restore(HttpServletRequest request, HttpServletResponse response) {
		usersService.restore(request, response);
	}
	
	/*
	 * @ResponseBody
	 * 
	 * @PostMapping(value="/checkPw.do", produces="application/json") // 사용자가 입력한
	 * 비밀번호가 맞는지 확인 public Map<String, Object> checkPw(HttpServletRequest request) {
	 * Map<String, Object> map = new HashMap<String, Object>(); map.put("isCorrect",
	 * usersService.checkPw(request)); return map; }
	 */
	
	@GetMapping("/mypage.do") // 마이페이지로 이동 
	public String mypage(HttpSession session, Model model) {
		String email = (String)session.getAttribute("loginEmail");
		model.addAttribute("loginUsers", usersService.getUsersByEmail(email));
		System.out.println(model);
		return "users/mypage";
	}
	
	@ResponseBody
	@PostMapping(value="/modifyPw.do", produces="application/json") // 비밀번호 변경 
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
	  
	  @ResponseBody
	  @PostMapping(value="/modifyInfo.do", produces="application/json")  // 개인정보 수정
	  public Map<String, Object> modifyInfo(HttpServletRequest request){
	    return usersService.modifyInfo(request);    
	  }
	  
	  @GetMapping("/naver/login.do")  // 네이버로그인
	  public String naverLogin(HttpServletRequest request, HttpServletResponse response, Model model) {
	    
	    /*
	      네이버로그인 2단계
	      1. 1단계에서 지정한 redirect_url이 "/users/naver/login.do"이므로 여기가 네이버로그인 2단계 지점이다.
	      2. 네이버회원 정보를 가져갈 수 있는 사용자인지 확인하기 위해서 clientId, cliendSecret 등의 정보를 네이버에게 전달한다.
	      3. 네이버는 사용자 정보가 확인되면 access_token이라는 임의의 값을 사용자에게 되돌려준다. 이 후 네이버회원 정보를 가져오려면 이 access_token값이 필요하다.
	      4. 2단계에서도 redirect_url을 지정하라고 하는데 이것은 네이버로그인이 모두 끝나면 어디로 이동할 것인지 앱의 주소를 적어주는 것이다.
	        메인 페이지로 이동하는 주소를 작성해 주고, 네이버개발자센터 > 내 애플리케이션 > "네이버 로그인 Callback URL (최대 5개)"에도 동일한 주소를 작성한다.
	      5. 2단계에서 사용하는 코드는 네이버개발자센터 > Documents > 네이버 로그인 > API 명세 > 로그인 API 명세 > "callback.jsp"에서 제공한다.
	    */
	    String accessToken = usersService.getNaverLoginToken(request);
	    
	    /*
	      네이버로그인 3단계
	      1. 2단계에서 획득한 access_token을 다시 네이버로 보내면 회원정보(profile)를 네이버가 보내준다.
	      2. 3단계에서 사용하는 코드는 네이버개발자센터 > Documents > 네이버 로그인 > API 명세 > 회원 프로필 조회 API 명세 > "Java" 예제에서 제공한다.
	    */
	    UsersDTO profile = usersService.getNaverLoginProfile(accessToken);   // 네이버로부터 받아온 프로필 정보

	    /*
	      네이버로그인 4단계
	      1. 3단계에서 획득한 회원정보(profile)를 이용해서 네이버로그인 후속 작업을 진행한다.
	      2. 네이버로그인을 처음 시도하는 사용자는 네이버간편가입 페이지로 이동해서 회원가입을 진행한다.
	      3. 네이버로그인을 이미 시도해서 회원가입된 사용자는 로그인을 진행한다.
	    */
	    
	    
	    UsersDTO naverUsers = usersService.getUsersByEmail(profile.getEmail()); // 네이버로그인을 이미 시도해서 회원가입된 사용자는 DB에 회원정보가 있다.
	   
	    
	    // 네이버로그인을 처음 시도하는 사용자 : 네이버간편가입 페이지로 이동한다. 이 때 회원정보(profile)을 전달해서 간편가입할 때 사용한다.
	    if(naverUsers == null) {
	      model.addAttribute("profile", profile);
	      return "users/naver_join";
	    }
	    // 네이버로그인을 이미 시도해서 회원가입된 사용자 : 로그인을 진행한다.
	    else {
	      usersService.naverLogin(request, response, naverUsers);
	      return "redirect:/";
	    }
	    
	  }
	  
	  
	  @PostMapping("/naver/join.do")  // 네이버로그인 회원을 위한 간편가입
	  public void naverJoin(HttpServletRequest request, HttpServletResponse response) {
	    
	    /*
	      네이버로그인 5단계
	      1. 네이버로그인을 처음 시도하는 사용자를 네이버간편가입 페이지에서 회원가입시킨다.
	      2. 일반회원가입과 다르게 처리하는 부분이 있다.
	        1) 아이디 : 네이버가 회원 식별을 위해 제공한 id를 사용한다. 임의의 값으로 되어 있기 때문에 화면에 공개되지 않도록 주의한다.
	                    (기존에는 로그인할 때 loginId를 session에 올리고 화면에 보여주는 방식을 사용했으나, 이제는 loginName(사용자이름)을 session에 추가로 올리고 화면에 보여주는 방식으로 변경하였다.)
	                    (네이버가 제공한 id는 40 BYTE 이상의 크기를 가지므로 ID 칼럼의 크기를 VARCHAR2(40 BYTE) -> VARCHAR2(50 BYTE)로 수정하였다.)
	        2) 비밀번호 : 회원정보 중 생년월일을 이용해서 초기 비번으로 설정해 준다.(20201217과 같은 형식의 8자리 비번)
	                      (현재 앱이 마이페이지를 들어갈 때 비밀번호를 입력하도록 되어 있으므로 네이버간편가입을 진행하는 회원에게 초기비밀번호에 대해서 알려주는 것이 필요하다.)
	    */
	    
	    /*
	      Tip.
	      앞으로 새로운 애플리케이션을 만들 때 주로 간편가입을 이용해서 회원가입을 처리하고자 한다면,
	      각 사이트에서 대부분 회원정보로 제공하는 이메일을 회원의 아이디로 사용하는 것이 좋고,
	      정식가입이든 간편가입이든 모든 회원이 사용할 수 있는 서비스에서는 회원의 비밀번호를 묻지 않도록 설계하는 것이 좋다.
	    */
	    
	    usersService.naverJoin(request, response);
	    
	  }
	  
	  @GetMapping("/kakao/login.do")  
	  public String kakaoLogin(HttpServletRequest request, HttpServletResponse response, Model model) {
	    
	 
	    String accessToken = usersService.getKakaoLoginToken(request);
	
	    UsersDTO profile = usersService.getKakaoLoginProfile(accessToken);   // 카카오로부터 받아온 프로필 정보
	    
	    UsersDTO kakaoUsers = usersService.getUsersByEmail(profile.getEmail()); // 카카오로그인을 이미 시도해서 회원가입된 사용자는 DB에 회원정보가 있다.
	   
	    
	    // 카카오로그인을 처음 시도하는 사용자 : 카카오간편가입 페이지로 이동한다. 이 때 회원정보(profile)을 전달해서 간편가입할 때 사용한다.
	    if(kakaoUsers == null) {
	      model.addAttribute("profile", profile);
	      return "users/kakao_join";
	    }
	    // 카카오로그인을 이미 시도해서 회원가입된 사용자 : 로그인을 진행한다.
	    else {
	      usersService.kakaoLogin(request, response, kakaoUsers);
	      return "redirect:/";
	    }
	    
	  }
	  
	  
	  @PostMapping("/kakao/join.do")  // 네이버로그인 회원을 위한 간편가입
	  public void kakaoJoin(HttpServletRequest request, HttpServletResponse response) {
	    
	    usersService.kakaoJoin(request, response);
	    
	  }
}	

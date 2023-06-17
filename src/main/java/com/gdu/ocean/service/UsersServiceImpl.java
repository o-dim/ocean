package com.gdu.ocean.service;

import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gdu.ocean.domain.OutUsersDTO;
import com.gdu.ocean.domain.UsersDTO;
import com.gdu.ocean.mapper.UsersMapper;
import com.gdu.ocean.util.JavaMailUtil;
import com.gdu.ocean.util.SecurityUtil;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@Service
public class UsersServiceImpl implements UsersService {
	
	//field
	 private final UsersMapper  usersMapper;
	 private final JavaMailUtil javaMailUtil;
	 private final SecurityUtil securityUtil;
	 
	@Override
	public Map<String, Object> verifyEmail(String email) {
		Map<String, Object> map = new HashMap<String, Object>();
	    map.put("enableEmail", usersMapper.selectUsersByEmail(email) == null && usersMapper.selectSleepUsersByEmail(email) == null && usersMapper.selectOutUsersByEmail(email) == null);
		return map;
	}
	
	@Override
	public Map<String, Object> verifyPhoneNo(String phoneNo) {
		Map<String, Object> map = new HashMap<String, Object>();
	    map.put("enablePhoneNo", usersMapper.selectUsersByPhoneNo(phoneNo) == null && usersMapper.selectSleepUsersByPhoneNo(phoneNo) == null && usersMapper.selectOutUsersByPhoneNo(phoneNo) == null);
		return map;
	}
	
	@Override
	public Map<String, Object> sendAuthCode(String email) {
		
		// 사용자에게 전송할 인증코드 6자리
		String authCode = securityUtil.getRandomString(6, true, true); // 6자리, 문자사용, 숫자사용
		
		// 사용자에게 메일보내기 
		javaMailUtil.sendJavaMail(email, "[OceanMusic] 인증요청 메일입니다.", "인증번호는 <strong>" +  authCode + "</strong>입니다.");
		
		// 사용자에게 전송한 인증코드를 join.html로 응답
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("authCode", authCode);
		return map;
	}
	
	@Override
	public void join(HttpServletRequest request, HttpServletResponse response) {
		
		// 요청 파라미터 
		String email = request.getParameter("email");
		String pw = request.getParameter("pw");
		String phoneNo = request.getParameter("phoneNo");
		String postcode = request.getParameter("postcode");
		String roadAddress = request.getParameter("roadAddress");
		String jibunAddress = request.getParameter("jibunAddress");
		String detailAddress = request.getParameter("detailAddress");
		String name = request.getParameter("name");
	    String location = request.getParameter("location");
	    String event = request.getParameter("event");
		
		// 비밀번호 SHA-256 암호화
		pw = securityUtil.getSha256(pw);
		
		// 이름 XSS 처리 
		name = securityUtil.preventXSS(name);
		
		// 상세주소 XSS 처리 
		detailAddress = securityUtil.preventXSS(detailAddress);
		
		// agreecode 
		int agreecode = 0; 
		if(location.isEmpty() == false && event.isEmpty() == false) {
			agreecode = 3;
		} else if(location.isEmpty() && event.isEmpty() == false) {
			agreecode = 2;
		} else if(location.isEmpty() == false && event.isEmpty()) {
			agreecode = 1; 
		}
		
		// UsersDTO 만들기 
		UsersDTO usersDTO = new UsersDTO();
		usersDTO.setEmail(email);
		usersDTO.setPw(pw);
		usersDTO.setPhoneNo(phoneNo);
		usersDTO.setPostcode(postcode);
		usersDTO.setRoadAddress(roadAddress);
		usersDTO.setJibunAddress(jibunAddress);
		usersDTO.setDetailAddress(detailAddress);
		usersDTO.setName(name);
		usersDTO.setAgreecode(agreecode);
		
		// 회원가입(UserDTO를 DB로 보내기) 
		int joinResult = usersMapper.insertUsers(usersDTO);
		
		// 응답 
		try { 
			
			response.setContentType("text/html; charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.println("<script>");
			if(joinResult == 1) {
				out.println("alert('회원 가입되었습니다.');");
				out.println("location.href='/index.html';");
			} else { 
				out.println("alert('회원 가입에 실패했습니다.');");
				out.println("history.go(-2);");
			}
			out.println("</script>");
			out.flush();
			out.close();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	@Override
	public void login(HttpServletRequest request, HttpServletResponse response) {
		
		// 요청 파라미터 
		String url = request.getParameter("url"); // 로그인 화면의 이전 주소(로그인 후 되돌아갈 주소)
		String email = request.getParameter("email");
		String pw = request.getParameter("pw");
		
		// 비밀번호 SHA-256 암호화 
		pw = securityUtil.getSha256(pw);
		
		// UsersDTO 만들기 
		UsersDTO usersDTO = new UsersDTO();
		usersDTO.setEmail(email);
		usersDTO.setPw(pw);
		
		// DB에서 UsersDTO 조회하기 
		UsersDTO loginUsersDTO = usersMapper.selectUsersByUsersDTO(usersDTO);
		
		// ID, PW가 일치하는 회원이 있으면 로그인 성공
	    // 0. 자동 로그인 처리하기(autologin 메소드에 맡기기)
	    // 1. session에 ID 저장하기
	    // 2. 회원 접속 기록 남기기
	    // 3. 이전 페이지로 이동하기
		
		if(loginUsersDTO != null) {
			
			/*
			 * // 자동 로그인 처리를 위한 autologin 메소드 호출하기 autologin(request, response);
			 * 
			 * HttpSession session = request.getSession();
			 * session.setAttribute("loginEmail", email);
			 * 
			 * int updateResult = usersMapper.updateUsersAccess(email); if(updateResult ==
			 * 0) { usersMapper.insertUsersAccess(email); }
			 * 
			 * try { response.sendRedirect(url); } catch (Exception e) {
			 * e.printStackTrace(); }
			 */
		}
		 // EMAIL, PW가 일치하는 회원이 없으면 로그인 실패
		else {
			// 응답 
			try {
				response.setContentType("text/html; charset=UTF-8");
				PrintWriter out = response.getWriter();
				out.println("<script>");
				out.println("alert('일치하는 회원 정보가 없습니다.');");
				out.println("location.href='/users/login.html';");
				out.println("</script>");
				out.flush();
				out.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	   @Override
	public void autologin(HttpServletRequest request, HttpServletResponse response) {
		
		   // 요청 파라미터 
		   String email = request.getParameter("email");
		   String chkAutologin = request.getParameter("chkAutologin");
		   
		   // 자동 로그인을 체크한 경우 
		   if(chkAutologin != null) {
			   
			   HttpSession session = request.getSession();
			   String sessionEmail = session.getId();
			   
			   // DB로 보낼 UsersDTO 만들기 
			   UsersDTO usersDTO = new UsersDTO();
			   usersDTO.setEmail(email);
			   usersDTO.setAutologinEmail(sessionEmail);
			   usersDTO.setAutologinExpiredAt(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 30));
			   
			   // DB로 UserDTO 보내기 
			   usersMapper.insertAutologin(usersDTO);
			   
			   // 쿠키 저장 
			   Cookie cookie = new Cookie("autologinEmail", sessionEmail);
			   cookie.setMaxAge(60 * 60 * 24 * 30);
			   cookie.setPath(request.getContextPath());
			   response.addCookie(cookie);
		   }
		   // 자동 로그인을 체크하지 않은 경우 
		   else {
			   
			   // DB에서 AUTOLOGIN_EMAIL 칼럼과 AUTOLOGIN_EXPIRED_AT 칼럼 정보 삭제하기
			   usersMapper.deleteAutologin(email);
			   
			   Cookie cookie = new Cookie("autologinEmail", "");
			   cookie.setMaxAge(0);
			   cookie.setPath(request.getContextPath());
			   response.addCookie(cookie);
		   }
	}
	
	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response) {
		
		// 1. 자동 로그인 해제    
		// DB에서 AUTOLOGIN_ID 칼럼과 AUTOLOGIN_EXPIRED_AT 칼럼 정보 삭제하기
		HttpSession session = request.getSession();
		String email = (String)session.getAttribute("loginEmail");
		usersMapper.deleteAutologin(email);
		
		// autoLoginId 쿠키 삭제하기
		Cookie cookie = new Cookie("autologinEmail", "");
		cookie.setMaxAge(0);
		cookie.setPath(request.getContextPath());
		response.addCookie(cookie);
		
		 // 2. session에 저장된 모든 정보 삭제 
		 session.invalidate();
	}
	
	@Transactional(readOnly=true)
	@Override
	public void out(HttpServletRequest request, HttpServletResponse response) {
		// 탈퇴할 회원의 Email은 Session에 loginEmail 속성으로 저장되어 있음. 
		HttpSession session = request.getSession();
		String email = (String)session.getAttribute("loginEmail");
		
		// 탈퇴할 회원의 정보(EMAIL, JOINED_AT) 가져오기
		UsersDTO usersDTO = usersMapper.selectUsersByEmail(email);
		
		// OutUsersDTO 만들기
		OutUsersDTO outUsersDTO = new OutUsersDTO();
		outUsersDTO.setEmail(usersDTO.getEmail());
		outUsersDTO.setJoinedAt(usersDTO.getJoinedAt());
		
		// 회원탈퇴
		int insertResult = usersMapper.insertOutUsers(outUsersDTO);
		int deleteResult = usersMapper.deleteUsers(email);
		
		// 응답 
		try {
			response.setContentType("text/html; charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.println("<script>");
			if(insertResult == 1 && deleteResult == 1) {
				// session 초기화
				session.invalidate();
				
				out.println("alert('다음에 또 뵐 수 있겠죠? 회원 탈퇴 되었습니다.')");
				out.println("location.href='/index.html';");
				
		} else {
			out.println("alert('회원 탈퇴에 실패했습니다.');");
			out.println("history.back()");
		} 
			out.println("</script>");
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Transactional(readOnly=true)
	@Override
	public void sleepUsersHandle() {
		int insertResult = usersMapper.insertSleepUsers();
		if(insertResult > 0) {
			usersMapper.deleteUsersForSleep();
		}
		
	}
	
	
	@Override
	public void restore(HttpServletRequest request, HttpServletResponse response) {
		
		 // 복원할 아이디는 session에 sleepUsersId로 저장되어 있다. 
	     HttpSession session = request.getSession();
	     String email = (String)session.getAttribute("sleepUsersEmail");
	     
	     // 복원 
	     int insertResult = usersMapper.insertRestoreUsers(email);
	     int deleteResult = usersMapper.deleteSleepUsers(email);
	     
	     // 응답 
	     try {
	    	 
	    	 response.setContentType("text/html; charset=UTF-8");
	    	 PrintWriter out = response.getWriter();
	    	 
	    	 out.println("<script>");
	    	 if(insertResult == 0 && deleteResult == 0) {
	    		 session.removeAttribute("sleepUsersEmail");
	    		 out.println("alert('다시 Ocean을 찾아 주셔서 감사합니다. 휴면 계정 활성화를 위해서 곧바로 로그인 해 주세요.');");
	    		 out.println("location.href='/index.html';");
		     } else { 
		    	 out.println("alert('휴면 계정이 복구되지 않았습니다. 다시 시도해 주세요.');");
		    	 out.println("history.back();");
		     }
	    	 out.println("</script>");
	    	 out.flush();
	    	 out.close();
	     } catch (Exception e) {
		   e.printStackTrace();
		}
	}
	
	@Override
	public boolean checkPw(HttpServletRequest request) {
		// 로그인한 사용자의 Email
		HttpSession session = request.getSession();
		String email = (String)session.getAttribute("loginEmail");
		
		// 로그인한 사용자의 정보를 가져옴 (비밀번호를 확인하기 위해서) 
		UsersDTO usersDTO = usersMapper.selectUsersByEmail(email);
		
		// 사용자가 입력한 PW
		String pw = securityUtil.getSha256(request.getParameter("pw"));
		
		// PW 비교 결과 반환 
		return pw.equals(usersDTO.getPw());
	
	}
	
	 @Override
	public UsersDTO getUserByEmail(String email) {
		return usersMapper.selectUsersByEmail(email);
	}
	 
	 @Override
	public Map<String, Object> modifyPw(HttpServletRequest request) {
		// 로그인한 사용자의 Email
		HttpSession session = request.getSession();
		String email = (String)session.getAttribute("loginEmail");
		
		// 사용자가 입력한 PW (이 PW로 비밀번호를 변경해야 한다.) 
		String pw = securityUtil.getSha256(request.getParameter("pw"));
		
		// Email과 PW를 가진 UsersDTO를 생성 
		UsersDTO usersDTO = new UsersDTO();
		usersDTO.setEmail(email);
		usersDTO.setPw(pw);
		
		// PW 수정 결과 반환 
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("pwUpdateResult", usersMapper.updateUsersPassword(usersDTO));
		return map;
	}
	 
	 @Override
	public Map<String, Object> findEmail(UsersDTO usersDTO) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("findUsers", usersMapper.selectUsersByEmail(usersDTO.getEmail()));
		return null;
	}
	 
	 @Override
	public Map<String, Object> findPw(UsersDTO usersDTO) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("findUsers", usersMapper.selectUsersByPhoneNo(usersDTO.getPhoneNo()));
		return null;
	}
	 
	 @Override
	public Map<String, Object> sendTempPw(UsersDTO usersDTO) {
		// 8자리 임시 비밀번호 생성 
		String tempPw = securityUtil.getRandomString(8, true, true);
		
		// DB로 보낼 UsersDTO (이메일이 일치하는 회원의 비밀번호 업데이트)
		usersDTO.setPw(securityUtil.getSha256(tempPw));
		
		// 임시비밀번호로 Users의 DB 정보 수정 
		int pwUpdateResult = usersMapper.updateUsersPassword(usersDTO);
		
		// 임시비밀번호로 Users의 DB 정보가 수정되면 이메일로 알림
		if(pwUpdateResult == 1) {
		
			// 메일 내용 
			String text = "";
			text += "<div>안녕하세요 Ocean Music입니다.";
			text += "<div>회원님의 요청으로 비밀번호가 초기화되었습니다.</div>";
			text += "<div>임시비밀번호는 <strong>" + tempPw + "입니다.</strong></div>";
			text += "<div>임시비밀번호로 로그인 후에 반드시 비밀번호를 변경해 주세요.</div>";
			text += "감사합니다.</div>";
			
			// 메일 전송 
			javaMailUtil.sendJavaMail(usersDTO.getEmail(), "[Ocean Music]임시 비밀번호 발급 안내", text);
		}
		
		// 결과 반환 
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("pwUpdateResult", pwUpdateResult);
		return map;
	}
	
}

package com.gdu.ocean.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gdu.ocean.domain.OutUsersDTO;
import com.gdu.ocean.domain.SleepUsersDTO;
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
      
      
      String message = "";
      message += "<div style=\"font-family: 'Apple SD Gothic Neo', 'sans-serif' !important; width: 400px; height: 500px; border-top: 4px solid gray; margin: 100px auto; padding: 30px 0; box-sizing: border-box;\">";
      message += "<span style=\"font-size: 15px; margin: 0 0 10px 3px;\">OCEAN Music</span><br />";
      message += "<span style=\"font-size: 15px; margin: 0 0 10px 3px;\">내가 찾던 <b style=\" color: #0B6138;\">'음악',</b>";
      message   += "<span style=\"font-size: 15px; margin: 0 0 10px 3px;\">내가 찾던 <b style=\" color: #0B6138;\">'OCEAN'</b><br /></span>";
      message += "<h1 style=\"margin: 0; padding: 0 5px; font-size: 28px; font-weight: 420; color: gray;\">";
      message += "<span style=\"color: #0B6138; font-weight:900;\">메일 인증</span> 안내입니다.";
      message += "</h1>\n";
      message += "<p style=\"font-size: 16px; line-height: 26px; margin-top: 50px; padding: 0 5px; color:gray;\">";
      message += "OCEAN Music에 가입해 주셔서 진심으로 감사드립니다.<br />";
      message += "아래 <b style=\" color: #0B6138;\">'인증 코드'</b>를 확인하고 인증코드를 입력하여 회원가입을 완료해 주세요.<br />";
      message += "감사합니다.";
      message += "</p>";
      message += "<p style=\"font-size: 16px; line-height: 26px; margin-top: 30px; padding: 0 5px; color:gray;\">";
      message += "<p style=\"display: inline-block; width: 210px; height: 45px; margin: 20px 5px 40px 90px; border: 1px solid gray; background-color: #0B6138; line-height: 45px; vertical-align: middle; font-size: 16px; color: white; text-align: center;\">";
      message += "<strong>" + authCode + "</strong></p>";
      message += "</a>";
      message += "<div style=\"border-top: 1px solid #DDD; padding: 5px;\"></div>";
      message += "</div>";
      
      // 사용자에게 메일 보내기 
      javaMailUtil.sendJavaMail(email, "[OceanMusic] 인증요청 메일입니다.", message);
      
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
      
      // 비밀번호 SHA-256 암호화
      pw = securityUtil.getSha256(pw);
      
      // 이름 XSS 처리 
      name = securityUtil.preventXSS(name);
      
      // 상세주소 XSS 처리 
      detailAddress = securityUtil.preventXSS(detailAddress);
      
       // agreecode
       int agreecode = 0;
       if(location.equals("on")) { agreecode += 1; }
      
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
      
      // 회원가입(UsersDTO를 DB로 보내기) 
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
      //String name = request.getParameter("name");
      String pw = request.getParameter("pw");
      
      /**********************************************************************/
      /****** 로그인 이전에 휴면계정(휴면 테이블에 정보가 있는지) 확인 ******/
      /****** SleepUserCheckInterceptor 코드를 옮겨온 뒤 인터셉터 제거 ******/
      /**********************************************************************/
      SleepUsersDTO sleepUsersDTO = usersMapper.selectSleepUsersByEmail(email);
      if(sleepUsersDTO != null) {                      // 휴면계정이라면(휴면 테이블에 정보가 있다면) 휴면복원페이지로 이동한다.
         HttpSession session = request.getSession();  // session에 sleepUsersEmail을 올려 놓으면 wakeup.html에서 휴면회원의 아이디를 확인할 수 있다.
         session.setAttribute("sleepUsersEmail", email);
         try {
            response.sendRedirect("/users/wakeup.html");  // 휴면복원페이지로 이동한다.
         } catch(Exception e) {
            e.printStackTrace();
         }
      }
      /**********************************************************************/
      
      // UsersDTO 만들기 
      UsersDTO usersDTO = new UsersDTO();
      usersDTO.setEmail(email);
      usersDTO.setPw(pw);
      
      // DB에서 UsersDTO 조회하기 
      UsersDTO loginUsersDTO = usersMapper.selectUsersByUsersDTO(usersDTO);
      
      // Email, PW가 일치하는 회원이 있으면 로그인 성공
       // 0. 자동 로그인 처리하기(autologin 메소드에 맡기기)
       // 1. session에 Email 저장하기
       // 2. 회원 접속 기록 남기기
       // 3. 이전 페이지로 이동하기
      
      if(loginUsersDTO != null) {
         
         // 자동 로그인 처리를 위한 autologin 메소드 호출하기
         autologin(request, response);
         
         HttpSession session = request.getSession();
         session.setAttribute("loginEmail", email);
         session.setAttribute("loginUsersNo", loginUsersDTO.getUserNo());
         session.setAttribute("loginUsersName", loginUsersDTO.getName());
         
         //session.setAttribute("loginUser", loginUsersDTO);  //  html : ${session.loginUser.email}   ${session.loginUser.name}  ... 
         
         int updateResult = usersMapper.updateUsersAccess(email);
         if(updateResult == 0) {
            usersMapper.insertUsersAccess(email);
         }
         
         try {
            response.sendRedirect(url);
         } catch (Exception e) {
            e.printStackTrace();
         }
         
      }
       // EMAIL, PW가 일치하는 회원이 없으면 로그인 실패
      else {
         // 응답 
         try {
            response.setContentType("text/html; charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.println("<script>");
            out.println("alert('일치하는 회원 정보가 없습니다.');");
            out.println("location.href='/users/login.html';"); // 로그인 실패 후 이동하는 주소
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
      
        /*
         자동 로그인 처리하기
         
         1. 자동 로그인을 체크한 경우
           1) session의 id를 DB의 AUTOLOGIN_EMAIL 칼럼에 저장한다. (중복이 없고, 다른 사람이 알기 어려운 정보를 이용해서 자동 로그인에서 사용할 ID를 결정한다.)
           2) 자동 로그인을 유지할 기간(예시 : 15일)을 DB의 AUTOLOGIN_EXPIRED_AT 칼럼에 저장한다.
           3) session의 id를 쿠키로 저장한다. (쿠키 : 각 사용자의 브라우저에 저장되는 정보)
              이 때 쿠키의 유지 시간을 자동 로그인을 유지할 기간과 동일하게 맞춘다.
         
         2. 자동 로그인을 체크하지 않은 경우
           1) DB에 저장된 AUTOLOGIN_EMAIL 칼럼과 AUTOLOGIN_EXPIRED_AT 칼럼의 정보를 삭제한다.
           2) 쿠키를 삭제한다.
       */

       // 요청 파라미터
       String email = request.getParameter("email");
       String chkAutologin = request.getParameter("chkAutologin");

       // 자동 로그인을 체크한 경우
       if (chkAutologin != null) {
          
           HttpSession session = request.getSession();
           String autologinEmail = session.getId();  // session.getId() : 쿠키이름 JSESSIONID으로 저장되는 정보이다. 브라우저가 새롭게 열릴때마다 자동으로 갱신되는 임의의 값이다.

           // DB로 보낼 UsersDTO 만들기
           UsersDTO usersDTO = new UsersDTO();
           usersDTO.setEmail(email);
           usersDTO.setAutologinEmail(autologinEmail);
           usersDTO.setAutologinExpiredAt(new Date());

           // DB로 UsersDTO 보내기
           usersMapper.insertAutologin(usersDTO);

           // 쿠키 저장
           Cookie cookie = new Cookie("autologinEmail", autologinEmail);  // 쿠키이름 autologinEmail으로 session.getId()값(쿠키 JSESSIONID값) 저장하기
           cookie.setMaxAge(60 * 60 * 24 * 30);
           cookie.setPath("/");
           response.addCookie(cookie);
           
       }
       
       // 자동 로그인을 체크하지 않은 경우
       else {
          
           // DB에서 AUTOLOGIN_EMAIL 칼럼과 AUTOLOGIN_EXPIRED_AT 칼럼 정보 삭제하기
           usersMapper.deleteAutologin(email);
           Cookie cookie = new Cookie("autologinEmail", "");
           cookie.setMaxAge(0);
           cookie.setPath("/");
           response.addCookie(cookie);
       }
       
   }
   
   @Override
   public void logout(HttpServletRequest request, HttpServletResponse response) {
      
      // 1. 자동 로그인 해제    
      // DB에서 AUTOLOGIN_EMAIL 칼럼과 AUTOLOGIN_EXPIRED_AT 칼럼 정보 삭제하기
      HttpSession session = request.getSession();
      String email = (String)session.getAttribute("loginEmail");
      usersMapper.deleteAutologin(email);
      
      // autoLoginEmail 쿠키 삭제하기
      Cookie cookie = new Cookie("autologinEmail", "");
      cookie.setMaxAge(0);
      cookie.setPath(request.getContextPath());
      response.addCookie(cookie);
      
       // 2. session에 저장된 모든 정보 삭제 
       session.invalidate();
   }
   
   @Transactional
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
   
   @Transactional
   @Override
   public void sleepUsersHandle() {
      int insertResult = usersMapper.insertSleepUsers();
      if(insertResult > 0) {
         usersMapper.deleteUsersForSleep();
      }
      
   }
   
   
   @Override
   public void restore(HttpServletRequest request, HttpServletResponse response) {
      
       // 복원할 아이디는 session에 sleepUsersEmail로 저장되어 있다. 
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
           if(insertResult == 1 && deleteResult == 1) {
              session.removeAttribute("sleepUsersEmail");
              out.println("alert('다시 Ocean을 찾아 주셔서 감사합니다. 로그인이 가능한 상태입니다.');");
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
      
      System.out.println(usersDTO.getPw());
      System.out.println(pw);
      
      // PW 비교 결과 반환 
      return pw.equals(usersDTO.getPw());
   
   }
   
    @Override
   public UsersDTO getUsersByEmail(String email) {
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
      map.put("updateUsersPasswordResult", usersMapper.updateUsersPassword(usersDTO));
      return map;
   }
    
     @Override
     public Map<String, Object> modifyInfo(HttpServletRequest request) {
       
       // 요청 파라미터
       String email = request.getParameter("email");
       String name = request.getParameter("name");
       String phoneNo = request.getParameter("phoneNo");
       String postcode = request.getParameter("postcode");
       String roadAddress = request.getParameter("roadAddress");
       String jibunAddress = request.getParameter("jibunAddress");
       String detailAddress = request.getParameter("detailAddress");
       String location = request.getParameter("location");  // on 또는 off
       String event = request.getParameter("event");        // on 또는 off
       
       // 이름 XSS 처리
       name = securityUtil.preventXSS(name);
       
      
       // 상세주소 XSS 처리
       detailAddress = securityUtil.preventXSS(detailAddress);
       
     
       // agreecode
       int agreecode = 0;
       if(location.equals("on")) { agreecode += 1; }
       if(event.equals("on"))    { agreecode += 2; }
       
       // UsersDTO 만들기
       UsersDTO usersDTO = new UsersDTO();
       usersDTO.setEmail(email);
       usersDTO.setName(name);
       usersDTO.setPhoneNo(phoneNo);
       usersDTO.setPostcode(postcode);
       usersDTO.setRoadAddress(roadAddress);
       usersDTO.setJibunAddress(jibunAddress);
       usersDTO.setDetailAddress(detailAddress);
       usersDTO.setAgreecode(agreecode);
       
       // Info 수정 결과 반환
       Map<String, Object> map = new HashMap<String, Object>();
       map.put("updateUsersInfoResult", usersMapper.updateUsersInfo(usersDTO));
       return map;
       
     }
    
    @Override
   public Map<String, Object> findEmail(UsersDTO usersDTO) {
      Map<String, Object> map = new HashMap<String, Object>();
      map.put("findUsers", usersMapper.selectUsersByPhoneNo(usersDTO.getPhoneNo()));
      return map;
   }
    
    @Override
   public Map<String, Object> findPw(UsersDTO usersDTO) {
      Map<String, Object> map = new HashMap<String, Object>();
      map.put("findUsers", usersMapper.selectUsersByEmail(usersDTO.getEmail()));
      return map;
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
         text += "<div style=\"font-family: 'Apple SD Gothic Neo', 'sans-serif' !important; width: 420px; height: 500px; border-top: 4px solid gray; margin: 100px auto; padding: 30px 0; box-sizing: border-box;\">";
         text += "<span style=\"font-size: 15px; margin: 0 0 10px 3px;\">OCEAN Music</span><br />";
         text += "<span style=\"font-size: 15px; margin: 0 0 10px 3px;\">내가 찾던 <b style=\" color: #0B6138;\">'음악',</b>";
         text   += "<span style=\"font-size: 15px; margin: 0 0 10px 3px;\">내가 찾던 <b style=\" color: #0B6138;\">'OCEAN'</b><br /></span>";
         text += "<h1 style=\"margin: 0; padding: 0 5px; font-size: 28px; font-weight: 400; color: gray;\">";
         text += "<span style=\"color: #0B6138; font-weight:900;\">임시 비밀번호</span> 안내입니다.";
         text += "</h1>\n";
         text += "<p style=\"font-size: 16px; line-height: 26px; margin-top: 50px; padding: 0 5px; color:gray;\">";
         text += "회원님의 요청으로 비밀번호가 초기화되었습니다.<br />";
         text += "아래 <b style=\" color: #0B6138;\">'임시 비밀번호'</b>를 통해 로그인이 가능합니다.<br />";
         text += "감사합니다. <br />";
         text += "※ 로그인 후 반드시 비밀번호 변경을 진행하시길 바랍니다.<br />";
         text += "</p>";
         text += "<p style=\"font-size: 16px; line-height: 26px; margin-top: 30px; padding: 0 5px; color:gray;\">";
         text += "<p style=\"display: inline-block; width: 210px; height: 45px; margin: 20px 5px 40px 90px; border: 1px solid gray; background-color: #0B6138; line-height: 45px; vertical-align: middle; font-size: 16px; color: white; text-align: center;\">";
         text += "<strong>" + tempPw + "</strong></p>";
         text += "</a>";
         text += "<div style=\"border-top: 1px solid #DDD; padding: 5px;\"></div>";
         text += "</div>";
         // 메일 전송 
         javaMailUtil.sendJavaMail(usersDTO.getEmail(), "[Ocean Music]임시 비밀번호 발급 안내", text);
      }
      
      // 결과 반환 
      Map<String, Object> map = new HashMap<String, Object>();
      map.put("pwUpdateResult", pwUpdateResult);
      return map;
   }



/******************************************************************************************/
/*** 네이버개발자센터 > Products > 네이버 로그인 > "네이버 로그인 API"를 이용 신청할 것 ***/
/*** 제공 정보 3가지 : 회원이름, 연락처 이메일 주소, 휴대전화번호 ***/
/******************************************************************************************/
@Value("${naver.client_id}")
private String naverClientId;

@Value("${naver.client_secret}")
private String naverClientSecret;

@Override
public String getNaverLoginApiURL(HttpServletRequest request) {
    
  String naverApi = null;
  
  try {
    
    String redirectURI = URLEncoder.encode("http://quddls6.cafe24.com/users/naver/login.do", "UTF-8");
    //String redirectURI = URLEncoder.encode("http://localhost:8080/users/naver/login.do", "UTF-8");
    SecureRandom secureRandom = new SecureRandom();
    String state = new BigInteger(130, secureRandom).toString();
    
    naverApi = "https://nid.naver.com/oauth2.0/authorize?response_type=code";
    naverApi += "&client_id=" + naverClientId;
    naverApi += "&redirect_uri=" + redirectURI;
    naverApi += "&state=" + state;
    
    HttpSession session = request.getSession();
    session.setAttribute("state", state);
    
  } catch (Exception e) {
    e.printStackTrace();
  }
  //System.out.println(apiURL);
  return naverApi;
  
}

@Override
public String getNaverLoginToken(HttpServletRequest request) {
  
  // access_token 받기
  
  String code = request.getParameter("code");
  String state = request.getParameter("state");
  
  String redirectURI = null;
  try {
    redirectURI = URLEncoder.encode("http://quddls6.cafe24.com/", "UTF-8");
    //redirectURI = URLEncoder.encode("http://localhost:8080/", "UTF-8");
  } catch(UnsupportedEncodingException e) {
    e.printStackTrace();
  }
  
  StringBuffer res = new StringBuffer();  // StringBuffer는 StringBuilder와 동일한 역할을 수행한다.
  try {
    
    String naverApiURL;
    naverApiURL = "https://nid.naver.com/oauth2.0/token?grant_type=authorization_code&";
    naverApiURL += "client_id=" + naverClientId;
    naverApiURL += "&client_secret=" + naverClientSecret;
    naverApiURL += "&redirect_uri=" + redirectURI;
    naverApiURL += "&code=" + code;
    naverApiURL += "&state=" + state;
    
    URL url = new URL(naverApiURL);
    HttpURLConnection con = (HttpURLConnection)url.openConnection();
    con.setRequestMethod("GET");
    int responseCode = con.getResponseCode();
    BufferedReader br;
    if(responseCode == 200) {
      br = new BufferedReader(new InputStreamReader(con.getInputStream()));
    } else {
      br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
    }
    String inputLine;
    while ((inputLine = br.readLine()) != null) {
      res.append(inputLine);
    }
    br.close();
    con.disconnect();
    
    /*
      res.toString() 출력 예시
      
      {
        "access_token":"AAAANipjD0VEPFITQ50DR__AgNpF2hTecVHIe9v-_uoyK5eP1mfdYX57bM3VTF_x4cWgz0v2fQlZsOOjl9uS0j8CLI4",
        "refresh_token":"2P9T9LTrnjaBf8XwF87a2UNUL4isfvk3QyLF8U1MDmju5ViiSXNSxii80ii8kvZWDiiYSiptFFYsuwqWl6C8n59NwoAEU6MmipfIis2htYMnZUlutzvRexh0PIZzzqqK3HlGYttJ",
        "token_type":"bearer",
        "expires_in":"3600"
      }
    */
  
  } catch (Exception e) {
    e.printStackTrace();
  }
    
  JSONObject obj = new JSONObject(res.toString());
  String accessToken = obj.getString("access_token");
  return accessToken;
  
}

@Override
public UsersDTO getNaverLoginProfile(String accessToken) {
  
  // accessToken을 이용해서 회원정보(profile) 받기
  String header = "Bearer " + accessToken;
  
  StringBuffer sb = new StringBuffer();
  
  try {
    
    String naverApi = "https://openapi.naver.com/v1/nid/me";
    URL url = new URL(naverApi);
    HttpURLConnection con = (HttpURLConnection)url.openConnection();
    con.setRequestMethod("GET");
    con.setRequestProperty("Authorization", header);
    int responseCode = con.getResponseCode();
    BufferedReader br;
    if(responseCode == 200) {
      br = new BufferedReader(new InputStreamReader(con.getInputStream()));
    } else {
      br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
    }
    String inputLine;
    while ((inputLine = br.readLine()) != null) {
      sb.append(inputLine);
    }
    br.close();
    con.disconnect();
    
    /*
      sb.toString()
      
      {
        "resultcode": "00",
        "message": "success",
        "response": {
          "id":"asdfghjklqwertyuiopzxcvbnmadfafrgbgfg",
          "email":"hahaha@naver.com",
          "mobile":"010-1111-1111",
          "mobile_e164":"+821011111111",
          "name":"\ubbfc\uacbd\ud0dc",
        }
      }
    */
    
  } catch (Exception e) {
    e.printStackTrace();
  }
  
  // 받아온 profile을 UsersDTO로 만들어서 반환
  UsersDTO usersDTO = null;
  try {
    
    JSONObject profile = new JSONObject(sb.toString()).getJSONObject("response");
    String name = profile.getString("name");
    String email = profile.getString("email");
    String phoneNo = profile.getString("mobile").replaceAll("-", "");
   
    
    usersDTO = new UsersDTO();
    usersDTO.setName(name);
    usersDTO.setEmail(email);
    usersDTO.setPhoneNo(phoneNo);

    
  } catch (Exception e) {
    e.printStackTrace();
  }
    
  return usersDTO;
  
}

@Transactional
@Override
public void naverLogin(HttpServletRequest request, HttpServletResponse response, UsersDTO naverUsersDTO) {
  
  /**********************************************************************/
  /****** 로그인 이전에 휴면계정(휴면 테이블에 정보가 있는지) 확인 ******/
  /****** SleepUsersCheckInterceptor 코드를 옮겨온 뒤 인터셉터 제거 ******/
  /**********************************************************************/
  String email = naverUsersDTO.getEmail();
  SleepUsersDTO sleepUsersDTO = usersMapper.selectSleepUsersByEmail(email);
  if(sleepUsersDTO != null) {                     // 휴면계정이라면(휴면 테이블에 정보가 있다면) 휴면복원페이지로 이동한다.
    HttpSession session = request.getSession();  // session에 sleepUserId를 올려 놓으면 wakeup.jsp에서 휴면회원의 아이디를 확인할 수 있다.
    session.setAttribute("sleepUsersEmail", email);
    try {
      response.sendRedirect("/users/wakeup.html");  // 휴면복원페이지로 이동한다.
    } catch(Exception e) {
      e.printStackTrace();
    }
  }
  /**********************************************************************/
  
  // 로그인 처리
  HttpSession session = request.getSession();
  String loginName = naverUsersDTO.getName();
  session.setAttribute("loginEmail", email);
  session.setAttribute("loginName", loginName);
  
  // 로그인 기록 남기기
  int updateResult = usersMapper.updateUsersAccess(email);
  if(updateResult == 0) {
    usersMapper.insertUsersAccess(email);
  }
  
}

@Override
public void naverJoin(HttpServletRequest request, HttpServletResponse response) {
  
  // 파라미터
  String email = request.getParameter("email");
  String name = request.getParameter("name");
  String phoneNo = request.getParameter("phoneNo");
  String location = request.getParameter("location");
  
  
  // 이름 XSS 처리
  name = securityUtil.preventXSS(name);
  
  // agreecode
  int agreecode = 0;
  if(location != null) { agreecode += 1; }
  
  // UsersDTO 만들기
  UsersDTO usersDTO = new UsersDTO();
  usersDTO.setEmail(email);
  usersDTO.setName(name);
  usersDTO.setPhoneNo(phoneNo);
  usersDTO.setAgreecode(agreecode);
      
  // 회원가입처리
  int naverJoinResult = usersMapper.insertNaverUsers(usersDTO);
  
  // 응답
  try {
    
    response.setContentType("text/html; charset=UTF-8");
    PrintWriter out = response.getWriter();
    out.println("<script>");
    if(naverJoinResult == 1) {
      out.println("alert('네이버 간편가입이 완료되었습니다.');");
      out.println("location.href='/index.html';");
    } else {
      out.println("alert('네이버 간편가입이 실패했습니다.');");
      out.println("history.go(-2);");
    }
    out.println("</script>");
    out.flush();
    out.close();
    
  } catch (Exception e) {
    e.printStackTrace();
       }
   }

   @Value("${kakaopay.rest_api_key}")
   private String kakaoRestApiKey;

   @Override
   public String getKakaoLoginApiURL(HttpServletRequest request) {
        String kakaoApi = null;
        
        try {
          
        String redirectURI = URLEncoder.encode("http://quddls6.cafe24.com/users/kakao/login.do", "UTF-8");
//          String redirectURI = URLEncoder.encode("http://localhost:8080/users/kakao/login.do", "UTF-8");
          SecureRandom secureRandom = new SecureRandom();
          String state = new BigInteger(130, secureRandom).toString();
          
          kakaoApi = "https://kauth.kakao.com/oauth/authorize?response_type=code&";
          kakaoApi += "client_id=" + kakaoRestApiKey;
          kakaoApi += "&redirect_uri=" + redirectURI;
          kakaoApi += "&state=" + state;
          
          HttpSession session = request.getSession();
          session.setAttribute("state", state);
          
        } catch (Exception e) {
          e.printStackTrace();
        }
        //System.out.println(apiURL);
        return kakaoApi;
        
      }

   
   @Override
   public String getKakaoLoginToken(HttpServletRequest request) {
        // access_token 받기
        
        String code = request.getParameter("code");
        String state = request.getParameter("state");
        
        String redirectURI = null;
        try {
          redirectURI = URLEncoder.encode("http://quddls6.cafe24.com/users/kakao/login.do", "UTF-8");
//          redirectURI = URLEncoder.encode("http://localhost:8080/users/kakao/login.do", "UTF-8");
        } catch(UnsupportedEncodingException e) {
          e.printStackTrace();
        }
        
        StringBuffer res = new StringBuffer();  // StringBuffer는 StringBuilder와 동일한 역할을 수행한다.
        try {
          
          String kakaoApiURL;
          kakaoApiURL = "https://kauth.kakao.com/oauth/token?grant_type=authorization_code&";
          kakaoApiURL += "client_id=" + kakaoRestApiKey;
          kakaoApiURL += "&redirect_uri=" + redirectURI;
          kakaoApiURL += "&code=" + code;
          kakaoApiURL += "&state=" + state;
          
          URL url = new URL(kakaoApiURL);
          HttpURLConnection con = (HttpURLConnection)url.openConnection();
          con.setRequestMethod("POST");
          int responseCode = con.getResponseCode();
          BufferedReader br;
          if(responseCode == 200) {
            br = new BufferedReader(new InputStreamReader(con.getInputStream()));
          } else {
            br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
          }
          String inputLine;
          while ((inputLine = br.readLine()) != null) {
            res.append(inputLine);
          }
          br.close();
          con.disconnect();
        
        } catch (Exception e) {
          e.printStackTrace();
        }
        System.out.println("토큰 객체 " + res.toString());
        JSONObject obj = new JSONObject(res.toString());
        String accessToken = obj.getString("access_token");
        return accessToken;
        
      }


@Override
public UsersDTO getKakaoLoginProfile(String accessToken) {
   
   // accessToken을 이용해서 회원정보(profile) 받기
     String header = "Bearer " + accessToken;
     
     StringBuffer sb = new StringBuffer();
     
     try {
       
       String kakaoApiURL = "https://kapi.kakao.com/v2/user/me";
       URL url = new URL(kakaoApiURL);
       HttpURLConnection con = (HttpURLConnection)url.openConnection();
       con.setRequestMethod("GET");
       con.setRequestProperty("Authorization", header);
       int responseCode = con.getResponseCode();
       BufferedReader br;
       if(responseCode == 200) {
         br = new BufferedReader(new InputStreamReader(con.getInputStream()));
       } else {
         br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
       }
       String inputLine;
       while ((inputLine = br.readLine()) != null) {
         sb.append(inputLine);
       }
       br.close();
       con.disconnect();
       
       /*
         sb.toString()
         
         {
           "resultcode": "00",
           "message": "success",
           "response": {
             "id":"asdfghjklqwertyuiopzxcvbnmadfafrgbgfg",
             "email":"hahaha@naver.com",
             "mobile":"010-1111-1111",
             "mobile_e164":"+821011111111",
             "name":"\ubbfc\uacbd\ud0dc",
           }
         }
       */
       
       System.out.println("객체 " + sb.toString());
       
     } catch (Exception e) {
       e.printStackTrace();
     }
     
     // 받아온 profile을 UsersDTO로 만들어서 반환
     UsersDTO usersDTO = null;
     try {
       
       JSONObject kakaoName = new JSONObject(sb.toString()).getJSONObject("properties");
       String name = kakaoName.getString("nickname");
       JSONObject kakaoEmail = new JSONObject(sb.toString()).getJSONObject("kakao_account");
       String email = kakaoEmail.getString("email");

       
       usersDTO = new UsersDTO();
       usersDTO.setName(name);
       usersDTO.setEmail(email);

       //System.out.println("userDTO"  + usersDTO);
     } catch (Exception e) {
       e.printStackTrace();
     }
       
     return usersDTO;
     
   }

   @Transactional
   @Override
   public void kakaoLogin(HttpServletRequest request, HttpServletResponse response, UsersDTO kakaoUsersDTO) {
     
     /**********************************************************************/
     /****** 로그인 이전에 휴면계정(휴면 테이블에 정보가 있는지) 확인 ******/
     /****** SleepUserCheckInterceptor 코드를 옮겨온 뒤 인터셉터 제거 ******/
     /**********************************************************************/
     String email = kakaoUsersDTO.getEmail();
     SleepUsersDTO sleepUsersDTO = usersMapper.selectSleepUsersByEmail(email);
     if(sleepUsersDTO != null) {                     // 휴면계정이라면(휴면 테이블에 정보가 있다면) 휴면복원페이지로 이동한다.
       HttpSession session = request.getSession();  // session에 sleepUserId를 올려 놓으면 wakeup.jsp에서 휴면회원의 아이디를 확인할 수 있다.
       session.setAttribute("sleepUsersEmail", email);
       try {
         response.sendRedirect("/users/wakeup.html");  // 휴면복원페이지로 이동한다.
       } catch(Exception e) {
         e.printStackTrace();
       }
     }
     /**********************************************************************/
     
     // 로그인 처리
     HttpSession session = request.getSession();
     String loginName = kakaoUsersDTO.getName();
     session.setAttribute("loginEmail", email);
     session.setAttribute("loginName", loginName);
     System.out.println(email + " "  + loginName);
     
     // 로그인 기록 남기기
     int updateResult = usersMapper.updateUsersAccess(email);
     if(updateResult == 0) {
       usersMapper.insertUsersAccess(email);
     }
     
   }
   
   @Override
   public void kakaoJoin(HttpServletRequest request, HttpServletResponse response) {
        // 파라미터
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String location = request.getParameter("location");
        
        
        // 이름 XSS 처리
        name = securityUtil.preventXSS(name);
        
        // agreecode
        int agreecode = 0;
        if(location != null) { agreecode += 1; }
        
        // UsersDTO 만들기
        UsersDTO usersDTO = new UsersDTO();
        usersDTO.setEmail(email);
        usersDTO.setName(name);
        usersDTO.setAgreecode(agreecode);
            
        // 회원가입처리
        int kakaoJoinResult = usersMapper.insertKakaoUsers(usersDTO);
        
        // 응답
        try {
          
          response.setContentType("text/html; charset=UTF-8");
          PrintWriter out = response.getWriter();
          out.println("<script>");
          if(kakaoJoinResult == 1) {
            out.println("alert('카카오 간편가입이 완료되었습니다.');");
            out.println("location.href='/index.html';");
          } else {
            out.println("alert('카카오 간편가입이 실패했습니다.');");
            out.println("history.go(-2);");
          }
          out.println("</script>");
          out.flush();
          out.close();
          
        } catch (Exception e) {
          e.printStackTrace();
             }
      
   }
}



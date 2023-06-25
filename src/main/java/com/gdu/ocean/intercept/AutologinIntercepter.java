package com.gdu.ocean.intercept;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.WebUtils;

import com.gdu.ocean.domain.UsersDTO;
import com.gdu.ocean.mapper.UsersMapper;

@Component
public class AutologinIntercepter implements HandlerInterceptor {

  // 로그인이 안 된 상태이고,
  // 쿠키에 autologinEmail 값이 존재하는 경우에
  // 자동 로그인을 수행하는 인터셉터
  
  @Autowired
  private UsersMapper usersMapper;
  
  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

    HttpSession session = request.getSession();
    
    if(session != null && session.getAttribute("loginEmail") == null) {  // 로그인이 되어 있는가?
      
      Cookie cookie = WebUtils.getCookie(request, "autologinEmail");
      
      if(cookie != null) {  // 쿠키 autologinEmail이 존재하는가?
        
        String autologinEmail = cookie.getValue();
        UsersDTO loginUsersDTO = usersMapper.selectAutologin(autologinEmail);
        if(loginUsersDTO != null) {
          session.setAttribute("loginEmail", loginUsersDTO.getEmail());
        }
      }
    }
    return true;  // 인터셉터를 동작 시킨 뒤 컨트롤러를 계속 동작시킨다.
  }
}
package com.gdu.ocean.config;

import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.gdu.ocean.intercept.LoginCheckInterceptor;
import com.gdu.ocean.util.MyFileUtil;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer{
	 //field
	private final LoginCheckInterceptor loginCheckInterceptor;
	private final MyFileUtil myFileUtil;
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(loginCheckInterceptor)
			   //.addPathPatterns("/bbs/write.html", "/upload/write.html")			
			    .addPathPatterns("/users/logout.do");
     	registry.addInterceptor(loginCheckInterceptor)
			 		
		        .excludePathPatterns("/users/out.do"); // 제외할 요청
	}
}

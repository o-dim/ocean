package com.gdu.ocean.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.gdu.ocean.intercept.LoginCheckInterceptor;
import com.gdu.ocean.util.MyFileUtil;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
	
	//field 
	private final LoginCheckInterceptor loginCheckInterceptor;
	private final MyFileUtil myFileUtil;

	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(loginCheckInterceptor)
				.addPathPatterns("/users/mypage.do")		
				.excludePathPatterns("/users/out.do", "/index.html"); // 제외할 요청 

	}
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/imageLoad/**")
				.addResourceLocations("file:" + myFileUtil.getSummernoteImagePath() + "/");
	}
}

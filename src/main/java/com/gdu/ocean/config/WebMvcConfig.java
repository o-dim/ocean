package com.gdu.ocean.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.gdu.ocean.intercept.AutologinIntercepter;
import com.gdu.ocean.intercept.LoginCheckInterceptor;
import com.gdu.ocean.util.MyFileUtil;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer{
    //field
   private final LoginCheckInterceptor loginCheckInterceptor;
   private final AutologinIntercepter autologinIntercepter;
   private final MyFileUtil myFileUtil;
   
   @Override
   public void addInterceptors(InterceptorRegistry registry) {
      registry.addInterceptor(loginCheckInterceptor)
            .addPathPatterns("/users/logout.do", "/users/mypage.do"); // 모든 요청 
             
        registry.addInterceptor(autologinIntercepter)
              .addPathPatterns("/**");
   }
   
   
   @Override
   public void addResourceHandlers(ResourceHandlerRegistry registry) {
      registry.addResourceHandler("/imageLoad/**")
       .addResourceLocations("file:" + myFileUtil.getSummernoteImagePath() + "/");
   }
}

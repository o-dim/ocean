<<<<<<< HEAD
package com.gdu.ocean.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;

import com.gdu.ocean.domain.HashtagDTO;
import com.gdu.ocean.domain.OrderDTO;


public interface ShopService {

	public void getCdList(HttpServletRequest request, Model model);
	public void getCdByNo(int cdNo, Model model);
	public List<HashtagDTO> getHashtagName(int cdNo);
	
	public Map<String, Object> addCartDetail(HttpServletRequest request);
	public void getCartDetailList(int cartNo, Model model);
	//public Map<String, Object> addOrderList(HttpServletRequest request);
	
	
	
}
=======
package com.gdu.ocean.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;

import com.gdu.ocean.domain.HashtagDTO;


public interface ShopService {

   public void getCdList(HttpServletRequest request, Model model);
   public void getCdByNo(int cdNo, Model model);
   public List<HashtagDTO> getHashtagName(int cdNo);
   
   public Map<String, Object> addCartDetail(HttpServletRequest request);
   public void getCartDetailList(int cartNo, Model model);
   
}
>>>>>>> fa6e8411a6c889010baf48b55e573e4559e4810a

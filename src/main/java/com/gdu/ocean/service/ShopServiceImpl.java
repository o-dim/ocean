package com.gdu.ocean.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.mail.Session;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.catalina.connector.Request;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.gdu.ocean.domain.CartDTO;
import com.gdu.ocean.domain.CartDetailDTO;
import com.gdu.ocean.domain.CdDTO;
import com.gdu.ocean.domain.HashtagDTO;
import com.gdu.ocean.domain.OrderDTO;
import com.gdu.ocean.domain.UsersDTO;
import com.gdu.ocean.mapper.ShopMapper;
import com.gdu.ocean.util.PageUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@Service
public class ShopServiceImpl implements ShopService {

	private final ShopMapper shopMapper;
	private final PageUtil pageUtil2;
	
	@Override
	public void getCdList(HttpServletRequest request, Model model) {
		
		Optional<String> opt1 = Optional.ofNullable(request.getParameter("type"));
		String type = opt1.orElse("");

		//파라미터 query가 전달되지 않는 경우 query=""로 처리
		Optional<String> opt2 = Optional.ofNullable(request.getParameter("searchText1"));
		String searchText1 = opt2.orElse("");
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("type", type);
		map.put("searchText1", searchText1);
		
		
		Optional<String> opt3 = Optional.ofNullable(request.getParameter("page"));
		int page = Integer.parseInt(opt3.orElse("1"));
		
		int totalRecord = shopMapper.getCdCount();
		int recordPerPage = 20;
		pageUtil2.setPageUtil(page, totalRecord, recordPerPage);

		map.put("begin", pageUtil2.getBegin());
		map.put("recordPerPage", recordPerPage);
		
		List<CdDTO> cdList = shopMapper.getCdList(map);
		model.addAttribute("cdList", cdList);
		model.addAttribute("beginNo", totalRecord - (page - 1) * recordPerPage);
		model.addAttribute("pagination", pageUtil2.getPagination("/shop/list.do?searchText1" + searchText1));
	}
	
	@Override
	public void getCdByNo(int cdNo, Model model) {
		model.addAttribute("cd", shopMapper.getCdByNo(cdNo));
	}
	
	@Override
	public List<HashtagDTO> getHashtagName(int cdNo) {
		return shopMapper.getHashtagName(cdNo);
	}
	
	@Override
	public Map<String, Object> directBuy(int userNo, int cdNo, int count, int cartNo) {
		CartDTO cartDTO = shopMapper.getCartByUserNo(userNo);
		// 파라미터 전달용 Map
		Map<String, Object> map = new HashMap<>();
		map.put("cartNo", cartDTO.getCartNo());
		map.put("cdNo", cdNo);
		map.put("count", count);
		
		Map<String, Object> resultMap = new HashMap<String,Object>();
		resultMap.put("directResult", shopMapper.addCartDetail(map));
		resultMap.put("cartNo", cartNo);
		
		return resultMap;
	}
	
	@Override
	public Map<String, Object> addCartDetail(HttpServletRequest request) {
		try {
			
			
			int userNo = Integer.parseInt(request.getParameter("userNo"));
			int cdNo = Integer.parseInt(request.getParameter("cdNo"));
			int count = Integer.parseInt(request.getParameter("count"));
		
			// 카트 존재 확인
			CartDTO cartDTO = shopMapper.getCartByUserNo(userNo);

			// 카트 없으면 만들기
			if(cartDTO == null) {
				int insertResult = shopMapper.madeCart(userNo);  // int 결과는 어차피 1 아니면 0
				if(insertResult == 1) {
					cartDTO = shopMapper.getCartByUserNo(userNo);
				}
			}
			
			// 파라미터 전달용 Map
			Map<String, Object> map = new HashMap<>();
			map.put("cartNo", cartDTO.getCartNo());
			map.put("cdNo", cdNo);
			map.put("count", count);

			// 이 장바구니에 이 CD를 장바구니에 담은 적이 있는지 점검
			CartDetailDTO cartDetailDTO = shopMapper.confirmCdInCart(map);
			
			// 장바구니 추가 및 수량 업데이트 결과
			int addCartDetailResult = 0;
			
			// 장바구니에 담은 적이 없는 CD이면 장바구니에 추가
			if(cartDetailDTO == null) {
				addCartDetailResult = shopMapper.addCartDetail(map);				
			}			
			// 이미 장바구니에 담은 CD이면 수량 업데이트
			else {
				addCartDetailResult = shopMapper.modifyCartDetail(map);
			}
			
			// 장바구니를 담고 이동하는데 필요한 것들 
			Map<String, Object> resultMap = new HashMap<String,Object>();
			resultMap.put("addCartDetailResult", addCartDetailResult);
			resultMap.put("cartNo", cartDTO.getCartNo());
			return resultMap;
		}catch (NumberFormatException e) {
			e.printStackTrace();
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
		
	}
	
	
	/*
	@Override
	public Map<String, Object> addOrderList(HttpServletRequest request) {
		int userNo = Integer.parseInt(request.getParameter("userNo"));
		log.info("userNo..........." + userNo);
		int cartNo = Integer.parseInt(request.getParameter("cartNo"));
		log.info("cartNo..........." + cartNo);
		//UsersDTO userDTO = shopMapper.getOrderNoByUserNo(cartNo);
		
		//OrderDTO orderDTO = shopMapper.getOrderNoByUserNo(userNo);
		UsersDTO usersDTO = shopMapper.getOrderNoByUserNo(cartNo);
		if(usersDTO == null) {
			Map<String, Object> map = new HashMap<String, Object>();
			int insertOrderResult = shopMapper.addOrderList(map);
			if(insertOrderResult == 1) {
				usersDTO = new UsersDTO();
			}	
			//파라미터 전달 이때 전달하고 나면 삭제해야함 
			map.put("userNo", usersDTO.getUserNo());
			map.put("cartNo", cartNo);
			
			OrderDTO orderDTO = shopMapper.confirmUsersInOrder(map);
			int addOrderResult = 0;
			
			
			Map<String, Object> orderResult = new HashMap<String, Object>();
			orderResult.put("addOrderResult", orderResult);
			orderResult.put("cartNo", cartNo);
			if(cartNo == 1) {
				shopMapper.deleteCart(cartNo);
			}
			return orderResult;
			
		}
		
		return null;
	}
	*/
		
	
	@Override
	public void getCartDetailList(int cartNo, Model model) {
		// getCartByCartNo -> 1번 보내서 1번 카트 가져오기 
		// public List<Map<String, Object>> getCartDetailList(int cartNo) {
		// getCartDetailByCartNo -> list로 끌고와야함 -> 안은 map으로 <"cdNo" : cdNo> <"count" count">
		// Map<String, Object> map = hashmap...
		// map.put("cdNo", cdNo)
		// map.put(
		//list.put(map) 
		// return list
		// 리스트는 model 쓰기가 애매하다 1개의 DTO면 모델을 쓸만하지만 여러개의 DTO 경우는 애매해다
		List<CartDetailDTO> cartDetailList = shopMapper.getCartDetailList(cartNo);
		model.addAttribute("cartDetailList", cartDetailList);
		model.addAttribute("cartNo", cartNo);
	}
	
	/*
		cart테이블
		cartNo madeAt userNo
		
		cartDetail테이블
		cartDetailNo userNo cartNo cdNo
		AI           userNo cartNo cdNo
	*/
	
}
package com.gdu.ocean.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.gdu.ocean.domain.CartDTO;
import com.gdu.ocean.domain.CartDetailDTO;
import com.gdu.ocean.domain.CdDTO;
import com.gdu.ocean.domain.HashtagDTO;
import com.gdu.ocean.domain.OrderDTO;
import com.gdu.ocean.domain.UsersDTO;

@Mapper
public interface ShopMapper {
	
	public int getCdCount();
	public List<CdDTO> getCdList(Map<String, Object> map); 			//cdList 가져오기 
	public CdDTO getCdByNo(int cdNo); 								//cdNo 가져오기
	public List<HashtagDTO> getHashtagName(int cdNo); 				//hashtag 가져오기
	
	public CartDTO getCartByUserNo(int userNo); 					//userNo를 이용해서 cart가져오기 
	public int madeCart(int userNo); 								//cart 만들기 
	
	public int addCartDetail(Map<String, Object> map);     			// 장바구니에 추가
	public int modifyCartDetail(Map<String, Object> map);  			// 장바구니 수량 변경
	
	public List<CartDetailDTO> getCartDetailNoByUserNo(int userNo); //cartdetail번호 가져오기
	public List<CartDetailDTO> getCartDetailList(int cartNo);		//cartDetail리스트 가져오기
	public CartDetailDTO getCartDetailByCartNo(int cartNo);
	public CartDetailDTO confirmCdInCart(Map<String, Object> map);
	
	public int addOrderList(Map<String, Object> map); 				//OrderList만들기 
	public List<OrderDTO> getOrderList(int userNo); 				//OrderList가져오기
	public UsersDTO getOrderNoByUserNo(int cartNo);					//cartNo를 통해서 orderNo가져오기
	public OrderDTO confirmUsersInOrder(Map<String, Object> map);
	public List<CartDTO> deleteCart(int cartNo);							//cartNo삭제하기 
}

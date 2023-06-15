package com.gdu.ocean.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.gdu.ocean.domain.CartDTO;
import com.gdu.ocean.domain.CartDetailDTO;
import com.gdu.ocean.domain.CdDTO;

@Mapper
public interface ShopMapper {
	
	public int getCdCount();
	public List<CdDTO> getCdList(Map<String, Object> map);
	public CdDTO getCdByNo(int cdNo);
	public List<CartDetailDTO> getCartList();
	public CartDetailDTO getCartListFK(int cartNo, int cdNo);
	
	
}

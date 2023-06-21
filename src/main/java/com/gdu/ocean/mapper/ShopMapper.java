package com.gdu.ocean.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.gdu.ocean.domain.CartDTO;
import com.gdu.ocean.domain.CartDetailDTO;
import com.gdu.ocean.domain.CdDTO;
import com.gdu.ocean.domain.HashtagCdDTO;
import com.gdu.ocean.domain.HashtagDTO;

@Mapper
public interface ShopMapper {
	
	public int getCdCount();
	public List<CdDTO> getCdList(Map<String, Object> map);
	public CdDTO getCdByNo(int cdNo);
	public List<HashtagDTO> getHashtagName(int cdNo);
	public List<CartDTO> getCartNo(int cartNo);
	public List<CartDetailDTO> getCartDetailNo(int cartDetailNo);
	public List<CdDTO> getcdSearch(Map<String, Object> map);
	//public CartDetailDTO getCartDetailNo(int cdNo);
	
	

	//public List<CartDTO> getCartList(Map<String, Object> map);	
}

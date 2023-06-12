package com.gdu.ocean.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.gdu.ocean.domain.CdDTO;

@Mapper
public interface ShopMapper {
	public int getShopCount();
	public List<CdDTO> getShopList(Map<String, Object> map);
}

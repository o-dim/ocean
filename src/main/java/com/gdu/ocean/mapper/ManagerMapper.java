package com.gdu.ocean.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.gdu.ocean.domain.CdDTO;
import com.gdu.ocean.domain.HashtagDTO;
import com.gdu.ocean.domain.UsersDTO;

@Mapper
public interface ManagerMapper {
	public int getUserCount();
	public int getCdCount();
	public List<CdDTO> getSaleList(Map<String, Object> map);
	public List<HashtagDTO> getHashtagByNo(String[] cdNo);
	public int removeCd(int cdNo);
	public List<HashtagDTO> getHashtagList();
	public List<UsersDTO> getUserListPagination(Map<String, Object> map);
	/* public int addCd(CdDTO cdDTO); */
}

package com.gdu.ocean.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.gdu.ocean.domain.CdDTO;
import com.gdu.ocean.domain.HashtagCdDTO;
import com.gdu.ocean.domain.HashtagDTO;
import com.gdu.ocean.domain.OrderDTO;
import com.gdu.ocean.domain.OutUsersDTO;
import com.gdu.ocean.domain.ReplyDTO;
import com.gdu.ocean.domain.SleepUsersDTO;
import com.gdu.ocean.domain.UsersDTO;

@Mapper
public interface ManagerMapper {
	public int getCdCount();
	public List<CdDTO> getSaleList(Map<String, Object> map);
	public List<HashtagDTO> getHashtagByNo(String[] cdNo);
	public int removeCd(int cdNo);
	public List<HashtagDTO> getHashtagList();
	public int getUserCount();
	public List<UsersDTO> getUserList(Map<String, Object> map);
	public int getUserSearchCount(Map<String, Object> map);
	public List<SleepUsersDTO> getSleepUserList(Map<String, Object> map);
	public int getSleepUserSearchCount(Map<String, Object> map);
	public List<OutUsersDTO> getOutUserList(Map<String, Object> map);
	public int getOutUserSearchCount(Map<String, Object> map);
	public UsersDTO selectUserById(String email);
	public int insertOutUser(UsersDTO usersDTO);
	public int deleteUser(String email);
	public int getBoardCount();
	public List<ReplyDTO> getBoardList(Map<String, Object> map);
	public int removeReply(int replyNo);
	public int addCd(CdDTO cdDTO);
	public int addHashtagCd(HashtagCdDTO hashtagCdDTO);
	public CdDTO getCdByNo(int cdNo);
	public int getOrderCount();
	public List<OrderDTO> getOrderList(Map<String, Object> map);
	public int getOrderSum();
}

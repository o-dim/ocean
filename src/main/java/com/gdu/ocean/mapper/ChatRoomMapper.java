package com.gdu.ocean.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.gdu.ocean.domain.ChatRoomDTO;

@Mapper
public interface ChatRoomMapper {
	// 채팅방 목록 불러오기
	public List<ChatRoomDTO> getAllChatRoom();
	
	// 채팅방 하나 불러오기
	public ChatRoomDTO findChatRoom(String roomId);
	// 채팅방 생성하기
	public ChatRoomDTO createChatRoom(String name);
}

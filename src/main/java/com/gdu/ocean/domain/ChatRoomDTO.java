package com.gdu.ocean.domain;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.springframework.web.socket.WebSocketSession;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoomDTO {
	//private String admin;
	private String roodId;
	private String name;
	private Set<WebSocketSession> websockeSessions = new HashSet<>();
	
	public static ChatRoomDTO create(String name) {
		ChatRoomDTO chatRoomDTO = new ChatRoomDTO();
		chatRoomDTO.roodId = UUID.randomUUID().toString();
		chatRoomDTO.name = name;
		return chatRoomDTO;
	}
}

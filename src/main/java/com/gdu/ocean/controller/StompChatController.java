package com.gdu.ocean.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.gdu.ocean.domain.ChatMessageDTO;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class StompChatController {
	// 특정 broker로 메세지 전달
	private final SimpMessagingTemplate template;
	
	// Client가 SEND할 수 있는 경로
    // stompConfig에서 설정한 applicationDestinationPrefixes와 @MessageMapping 경로가 병합됨
	@MessageMapping(value = "/chat/enter")
	public void enter(ChatMessageDTO messageDTO) {
		messageDTO.setMessage(messageDTO.getWriter() + "님이 채팅방에 입장하셨습니다.");
		template.convertAndSend("/sub/chat/room/" + messageDTO.getRoomId(), messageDTO);
	}
	
	@MessageMapping(value = "/chat/message")
	public void message(ChatMessageDTO messageDTO) {
		template.convertAndSend("/sub/chat/room/" + messageDTO.getRoomId(), messageDTO);
	}
	
}

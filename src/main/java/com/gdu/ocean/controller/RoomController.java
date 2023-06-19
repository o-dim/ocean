package com.gdu.ocean.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gdu.ocean.service.ChatRoomRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/chat")
@Log4j2
public class RoomController {
	private final ChatRoomRepository chatRoomRepository;

	// 채팅방 목록 조회 
	@GetMapping(value = "/rooms")
	public String rooms(Model model){
		log.info("# 모든 채팅방 불러오기............");
		model.addAttribute("list", chatRoomRepository.findAllRooms());
		return "/chat/rooms";
	}
	
	// 채팅방 개설
	@PostMapping(value="/room")
	public String create(@RequestParam String name, RedirectAttributes redirectAttributes) {
		log.info("# 새 채팅방 만들기 , name : " + name);
		redirectAttributes.addFlashAttribute("roomName", chatRoomRepository.createChatRoomDTO(name));
		return "redirect:/chat/rooms";
	}
	
//	// 채팅방 입장화면
//	@GetMapping("/room/enter/{roomId}")
//    public String roomDetail(Model model, @PathVariable String roomId) {
//        model.addAttribute("roomId", roomId);
//        return "/chat/rooms";
//    }
//	
	// 채팅방 조회
    @GetMapping("/room")
    public void getRoom(String roomId, Model model) {
        log.info("# 채팅방 조회 , roomId : " + roomId);
        
        model.addAttribute("room", chatRoomRepository.findRoomById(roomId));
    }

}

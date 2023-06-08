package com.gdu.ocean.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gdu.ocean.domain.SpotifyTrackDTO;
import com.gdu.ocean.service.SpotifyService;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@Controller
public class SpotifyController {
	
	private final SpotifyService spotifyService;
	
	@GetMapping(value="/searchArtist.do", produces = "application/json")
	@ResponseBody
	public List<SpotifyTrackDTO> searchArtist(HttpServletRequest request) {
		String keyword = request.getParameter("keyword");
		return spotifyService.SearchArtist(keyword);
	}
	
//	@GetMapping(value="/searchSong.do", produces = "application/json")
//	@ResponseBody
//	public List<SpotifyTrackDTO> searchSong(HttpServletRequest request) {
//		String keyword = request.getParameter("keyword");
//		return spotifyService.SearchSong(keyword);
//	}
}

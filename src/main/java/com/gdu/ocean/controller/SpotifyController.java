package com.gdu.ocean.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gdu.ocean.domain.SpotifyTrackDTO;
import com.gdu.ocean.service.SpotifyService;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@Controller
@RequestMapping("/song")
public class SpotifyController {
	
	private final SpotifyService spotifyService;
	
	@GetMapping("/myplaylist.html")
	public String myplaylist() {
		return "/song/myplaylist.html";
	}

    @GetMapping(value="/search-artist.do", produces="application/json")
    @ResponseBody
    public List<SpotifyTrackDTO> searchArtist(@RequestParam("keyword") String keyword) {
        return spotifyService.searchArtist(keyword);
    }
    
    @GetMapping("/callback")
    public String callback() {
    	return "/song/callback.html";
    }
    
	
//	@GetMapping(value="/searchSong.do", produces = "application/json")
//	@ResponseBody
//	public List<SpotifyTrackDTO> searchSong(HttpServletRequest request) {
//		String keyword = request.getParameter("keyword");
//		return spotifyService.SearchSong(keyword);
//	}
}

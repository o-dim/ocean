package com.gdu.ocean.spotify;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gdu.ocean.service.SpotifyService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/song")
public class SpotifyController {
	
	private final SpotifyService spotifyService;
	
	@GetMapping("/myplaylist.do")
	public String myplaylist(@RequestParam("id") String id) {
		return "/song/myplaylist.html";
	}

	@GetMapping("/spotifysearch.do")
	@ResponseBody
	public Map<String, Object> spotifysearch(@RequestParam("q") String q) {
		return spotifyService.spotifySearch(q);
	}
    
	@GetMapping(value="/recommendsong.do", produces="application/json")
	@ResponseBody
	public List<Map<String, String>> recommendsong(@RequestParam("mood") String mood) {
		return spotifyService.getRecommendsong(mood);
	}
	
    @GetMapping("/callback")
    public String callback() {
    	return "/song/callback.html";
    }
}
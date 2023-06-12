package com.gdu.ocean.spotify;

import org.springframework.http.ResponseEntity;
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
	
	@GetMapping("/myplaylist.html")
	public String myplaylist() {
		return "/song/myplaylist.html";
	}

	@GetMapping("/spotifysearch")
	@ResponseBody
	public ResponseEntity<SpotifySearch> searchArtist(@RequestParam("q") String q) {
		spotifyService.spotifySearch(q);
		return ResponseEntity.ok(null);
	//public List<Map<String, Object>> searchArtist(@RequestParam("q") String query) {
	  //  return spotifySearch.searchTracks_Sync(query);
	}
    
    @GetMapping("/callback")
    public String callback() {
    	return "/song/callback.html";
    }
}
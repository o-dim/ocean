package com.gdu.ocean.service;

import java.util.List;
import java.util.Map;

public interface SpotifyService {
	public Map<String, Object> spotifySearch(String q);
	// public Map<String, Object> spotifyPlaylist();
	public List<Map<String, String>> getRecommendsong(String mood);
}

package com.gdu.ocean.service;

import java.util.List;

import com.gdu.ocean.domain.SpotifyTrackDTO;

public interface SpotifyService {
	public List<SpotifyTrackDTO> SearchArtist(String keyword);
	// public List<SpotifyTrackDTO> SearchSong(String keyword);
}

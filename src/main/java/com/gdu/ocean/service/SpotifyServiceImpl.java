package com.gdu.ocean.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hc.core5.http.ParseException;
import org.springframework.stereotype.Service;

import com.gdu.ocean.config.SpotifyTokenConfig;
import com.gdu.ocean.domain.SpotifyTrackDTO;

import lombok.RequiredArgsConstructor;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.specification.ArtistSimplified;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.model_objects.specification.Track;
import se.michaelthelin.spotify.requests.data.search.simplified.SearchTracksRequest;

@RequiredArgsConstructor
@Service
public class SpotifyServiceImpl implements SpotifyService {
	
	private final SearchTracksRequest searchTracksRequest;
	private final SpotifyApi spotifyApi;
	
	@Override
	public List<SpotifyTrackDTO> SearchArtist(String keyword) {
		
		spotifyApi.searchArtists(keyword);
		
		List<SpotifyTrackDTO> tracklist = new ArrayList<>();
		ArtistSimplified artist = null;
		try {
			final Paging<Track> trackPaging = searchTracksRequest.execute();
			Track[] tracks = trackPaging.getItems();
//			track = trackPaging.getItems()[0];
//			spotifyTrackDTO.setTitle(track.getName());
//			spotifyTrackDTO.setAlbumName(track.getAlbum().getName());
//			spotifyTrackDTO.setImageUrl(track.getAlbum().getImages());
//			artist = track.getArtists()[0];
//			spotifyTrackDTO.setArtistName(artist.getName());
//			tracklist.add(spotifyTrackDTO);
			if (tracks.length > 0) {
			    Track track = tracks[0];
			    SpotifyTrackDTO spotifyTrackDTO = new SpotifyTrackDTO();
			    spotifyTrackDTO.setTitle(track.getName());
			    spotifyTrackDTO.setAlbumName(track.getAlbum().getName());
			    spotifyTrackDTO.setImageUrl(track.getAlbum().getImages());
			    spotifyTrackDTO.setArtistName(track.getArtists()[0].getName());

			    tracklist.add(spotifyTrackDTO);
			}
			
		} catch (IOException | SpotifyWebApiException | ParseException e) {
		}
		return tracklist;
	}

//	@Override
//	public List<SpotifyTrackDTO> SearchSong(String keyword) {
//		
//		List<SpotifyTrackDTO> tracklist = new ArrayList<>();
//		Track track = null;
//		
//		try {
//			final Paging<Artist> trackPaging = searchArtistRequest.execute();
//
//			track = trackPaging.getItems()[0];
//			spotifyTrackDTO.setArtistName(track.getName());
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
//		return null;
//	}
}

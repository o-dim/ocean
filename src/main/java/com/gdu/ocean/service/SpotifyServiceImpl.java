package com.gdu.ocean.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.hc.core5.http.ParseException;
import org.springframework.stereotype.Service;

import com.gdu.ocean.config.SpotifyConfig;
import com.gdu.ocean.domain.SpotifyTrackDTO;

import lombok.RequiredArgsConstructor;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.specification.ArtistSimplified;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.model_objects.specification.Track;
import se.michaelthelin.spotify.requests.data.search.simplified.SearchTracksRequest;

@RequiredArgsConstructor
@Service
public class SpotifyServiceImpl implements SpotifyService {
	

	private SpotifyConfig spotifyConfig;
	
	public SpotifyServiceImpl(SpotifyConfig spotifyConfig) {
        this.spotifyConfig = spotifyConfig;
    }
	
	@Override
	public List<SpotifyTrackDTO> searchArtist(String keyword) {
		List<SpotifyTrackDTO> tracklist = new ArrayList<>();
        try {
            SearchTracksRequest searchRequest = spotifyConfig.searchTracksRequest();
            final Paging<Track> trackPaging = searchRequest.execute();
            Track[] tracks = trackPaging.getItems();
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
            // 예외 처리
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

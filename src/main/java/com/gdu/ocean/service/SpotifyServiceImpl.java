package com.gdu.ocean.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.gdu.ocean.spotify.AccessToken;
import com.neovisionaries.i18n.CountryCode;
import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.model_objects.specification.ArtistSimplified;
import com.wrapper.spotify.model_objects.specification.Paging;
import com.wrapper.spotify.model_objects.specification.Track;
import com.wrapper.spotify.requests.data.search.simplified.SearchTracksRequest;

@Service
public class SpotifyServiceImpl implements SpotifyService {
	
	@Override
	public void spotifySearch(String q) {
		
		// 검색해서 가수 노래 나오게 하기
	    SpotifyApi spotifyApi = new SpotifyApi.Builder()
	    .setAccessToken(AccessToken.accessToken())
	    .build();

		SearchTracksRequest searchTracksRequest = spotifyApi.searchTracks(q)
				.market(CountryCode.KR)
		          .limit(10)
		          .offset(0)
		          .includeExternal("audio")
				.build();
			
		String preview = "";
		Track track = null;
		ArtistSimplified artist = null;
		List<Map<String, Object>> list = new ArrayList<>(); 
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			
			final Paging<Track> trackPaging = searchTracksRequest.execute();

			for (int i = 0; i < 10; i++) {
				
			}
//				System.out.println("Total: " + trackPaging.getTotal());
			track = trackPaging.getItems()[0];	//해당가수의 첫번째 음악
			
			String title = track.getName();
			String singer = track.getArtists()[0].getName();
			System.out.println("제목 : "+track.getName());
			System.out.println("가수 : "+track.getArtists()[0].getName());
            System.out.println(track.getAlbum().getImages()[0].getUrl());
			artist=trackPaging.getItems()[0].getArtists()[0];	//해당 노래를 부르는 메인 가수
			preview = trackPaging.getItems()[0].getPreviewUrl();	//미리듣기
			System.out.println("미리듣기 : " +preview);
            
			
			map.put("title", title);
			map.put("singer", singer);
			list.add(map);
//				artist=trackPaging.getItems()[0].getArtists()[0];	//해당 노래를 부르는 메인 가수
//				preview = trackPaging.getItems()[0].getPreviewUrl();	//미리듣기
//				System.out.println("미리듣기 : " +preview);
			

		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}

	}
	
}

package com.gdu.ocean.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.gdu.ocean.spotify.AccessToken;
import com.neovisionaries.i18n.CountryCode;
import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.model_objects.specification.Paging;
import com.wrapper.spotify.model_objects.specification.Track;
import com.wrapper.spotify.requests.data.search.simplified.SearchTracksRequest;

@Service
public class SpotifyServiceImpl implements SpotifyService {
	
	@Override
	public Map<String, Object> spotifySearch(String q) {
		
		int limit = 20;
		
		// 검색해서 가수 노래 나오게 하기
	    SpotifyApi spotifyApi = new SpotifyApi.Builder()
	    .setAccessToken(AccessToken.accessToken())
	    .build();

		SearchTracksRequest searchTracksRequest = spotifyApi.searchTracks(q)
				.market(CountryCode.KR)
		          .limit(limit)
		          .offset(0)
		          .includeExternal("audio")
				.build();
			
		Track track = null;
		List<Map<String, String>> songList = new ArrayList<>(); 
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		try {
			
			final Paging<Track> trackPaging = searchTracksRequest.execute();
			int total = trackPaging.getTotal(); // 검색해서 나온 총 갯수
			
			resultMap.put("total", total);
			
			for (int i = 0; i < limit; i++) {
				
				track = trackPaging.getItems()[i];	// 해당가수의 첫번째 음악
				
				String title = track.getName();                             // 제목 
				String singer = track.getArtists()[0].getName();            // 이 노래 가수이름
				String imgUrl = track.getAlbum().getImages()[0].getUrl();   // 앨범이미지
				String preview = trackPaging.getItems()[0].getPreviewUrl();	//미리듣기
				
				Map<String, String> song = new HashMap<>();
				song.put("title", title);
				song.put("singer", singer);
				song.put("imgUrl", imgUrl);
				song.put("preview", preview);
				
				songList.add(song);
				
			}
			
			resultMap.put("songList", songList);

		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}
		
		return resultMap;

	}
	
}

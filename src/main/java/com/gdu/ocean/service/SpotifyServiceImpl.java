package com.gdu.ocean.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.gdu.ocean.spotify.AccessToken;
import com.neovisionaries.i18n.CountryCode;
import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.model_objects.specification.Paging;
import com.wrapper.spotify.model_objects.specification.Recommendations;
import com.wrapper.spotify.model_objects.specification.Track;
import com.wrapper.spotify.model_objects.specification.TrackSimplified;
import com.wrapper.spotify.requests.data.browse.GetRecommendationsRequest;
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
				String preview = trackPaging.getItems()[0].getPreviewUrl();	// 미리듣기
				String music = trackPaging.getItems()[0].getUri();
				String music2 = track.getHref();
				track.getUri();
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
		
		return resultMap;	// Map 안에 리스트가 들어있다. resultMap는 total과 songList라는 두 개의 항목을 가지고 있다. 
							// total은 검색 결과의 총 개수를 나타내는 정수 값이고, songList는 노래 정보를 담은 리스트 
							// 각각의 노래 정보는 title, singer, imgUrl, preview라는 키를 가지고 있는 맵 객체로 구성

	}
	
	// uri, name, artist, album, image, 
//	@Override
//	public Map<String, Object> spotifyPlaylist() {
//		
//		SpotifyApi spotifyApi = new SpotifyApi.Builder()
//			    .setAccessToken(AccessToken.accessToken())
//			    .build();
//		
//		int limit = 20;
//		LocalDateTime now = LocalDateTime.now();
//		String playlist_id = now.getHour() + "" + now.getMinute() + "" + now.getSecond();
//		List<String> selectSongs = new ArrayList<>();
//		selectSongs.add(e)
//		
//		PlaylistTrack playlistTrack = spotifyApi.addItemsToPlaylist(playlist_id, uris));
//		 
//		 Track track = null;
//		
//		return null;
//	}
	
	@Override
	public List<Map<String, String>> getRecommendsong(String mood){

		  SpotifyApi spotifyApi = new SpotifyApi.Builder()
		    .setAccessToken(AccessToken.accessToken())
		    .build();

		  
		  GetRecommendationsRequest getRecommendationsRequest = spotifyApi.getRecommendations()
	          .limit(30)
	          .market(CountryCode.KR)
	          .max_popularity(100)
	          .min_popularity(80)
	          .seed_genres(mood)
//	          .target_popularity(20)
	          .build();
		  
		List<Map<String, String>> songList = new ArrayList<>(); 
		  
	    try {
	    	
	      final Recommendations recommendations = getRecommendationsRequest.execute();
	      
	      for(TrackSimplified track :  recommendations.getTracks()) {
			String preview = track.getPreviewUrl();
			String title = track.getName();                     // 제목 
			String singer = track.getArtists()[0].getName();    // 이 노래 가수이름
			Map<String, String> song = new HashMap<>();
			song.put("title", title);
			song.put("singer", singer);
			song.put("preview", preview);
			
			songList.add(song);
	      
	      }
			
	    } catch (Exception e) {
		      e.printStackTrace();
	    }

	    return songList;
	    
	}
	
		
}

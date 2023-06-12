package com.gdu.ocean.spotify;
import com.neovisionaries.i18n.CountryCode;
import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.specification.Artist;
import com.wrapper.spotify.model_objects.specification.ArtistSimplified;
import com.wrapper.spotify.model_objects.specification.Paging;
import com.wrapper.spotify.model_objects.specification.Track;
import com.wrapper.spotify.requests.data.search.simplified.SearchArtistsRequest;
import com.wrapper.spotify.requests.data.search.simplified.SearchTracksRequest;

import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.impl.bootstrap.HttpServer;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.tags.RequestContextAwareTag;

import java.io.IOException;
import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

@Service
public class SpotifySearch {
	
    // 검색해서 가수 노래 나오게 하기
    static SpotifyApi spotifyApi = new SpotifyApi.Builder()
    .setAccessToken(AccessToken.accessToken())
    .build();

    public List<Map<String, Object>> searchTracks_Sync(String q) {
		SearchTracksRequest searchTracksRequest = spotifyApi.searchTracks(q)
				.market(CountryCode.KR)
		          .limit(10)
//		          .offset(0)
//		          .includeExternal("audio")
				.build();
		String preview = "";
		Track track=null;
		ArtistSimplified artist=null;
		List<Map<String, Object>> list = new ArrayList<>(); 
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			final Paging<Track> trackPaging = searchTracksRequest.execute();

//			System.out.println("Total: " + trackPaging.getTotal());
			track = trackPaging.getItems()[0];	//해당가수의 첫번째 음악
			
			String title = track.getName();
			String singer = track.getArtists()[0].getName();
            
			
			map.put("title", title);
			map.put("singer", singer);
			list.add(map);
//			artist=trackPaging.getItems()[0].getArtists()[0];	//해당 노래를 부르는 메인 가수
//			preview = trackPaging.getItems()[0].getPreviewUrl();	//미리듣기
//			System.out.println("미리듣기 : " +preview);
			

		} catch (IOException | SpotifyWebApiException | ParseException e) {
			System.out.println("Error: " + e.getMessage());
		}
		return list;
	}
    
	
}

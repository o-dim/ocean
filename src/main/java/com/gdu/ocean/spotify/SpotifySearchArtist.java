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
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class SpotifySearchArtist {
    static String q = "Abba";	
    // 특정 가수 검색해서 가수 노래 나오게 하기
    static SpotifyApi spotifyApi = new SpotifyApi.Builder()
    .setAccessToken(AccessToken.accessToken())
    .build();

    public static ArtistSimplified searchTracks_Sync(String q) {
		SearchTracksRequest searchTracksRequest = spotifyApi.searchTracks(q)
				.market(CountryCode.KR)
		          .limit(10)
//		          .offset(0)
//		          .includeExternal("audio")
				.build();
		String preview = "";
		Track track=null;
		ArtistSimplified artist=null;
		try {
			final Paging<Track> trackPaging = searchTracksRequest.execute();

//			System.out.println("Total: " + trackPaging.getTotal());
			track=trackPaging.getItems()[0];	//해당가수의 첫번째 음악
			
			System.out.println("제목 : "+track.getName());
			System.out.println("가수 : "+track.getArtists()[0].getName());
            
			artist=trackPaging.getItems()[0].getArtists()[0];	//해당 노래를 부르는 메인 가수
			preview = trackPaging.getItems()[0].getPreviewUrl();	//미리듣기
			System.out.println("미리듣기 : " +preview);
		} catch (IOException | SpotifyWebApiException | ParseException e) {
			System.out.println("Error: " + e.getMessage());
		}
		return artist;
	}
    public static void main(String[] args) {
    ArtistSimplified artist=searchTracks_Sync("Abba");
  }
	
}

package com.gdu.ocean.spotify;

import java.util.concurrent.Future;

import com.gdu.ocean.service.SpotifyService;
import com.neovisionaries.i18n.CountryCode;

import lombok.RequiredArgsConstructor;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.model_objects.specification.Track;
import se.michaelthelin.spotify.requests.data.tracks.GetTrackRequest;

@RequiredArgsConstructor
public class SpotifyClass {
	
	// For all requests an access token is needed
	SpotifyApi spotifyApi = new SpotifyApi.Builder()
	        .setAccessToken(AccessToken.accessToken())
	        .build();

	// Create a request object with the optional parameter "market"
	final GetTrackRequest getTrackRequest = spotifyApi.getTrack("SPICY")
	        .market(CountryCode.KR)
	        .build();

	void getTrack_Sync() {
	  try {
	    // Execute the request synchronous
	    final Track tracks = getTrackRequest.execute();

	    // Print something's name
	    System.out.println("Name: " + tracks.getName());
	  } catch (Exception e) {
	    System.out.println("Something went wrong!\n" + e.getMessage());
	  }
	}

	void getTrack_Async() {
	  try {
	    // Execute the request asynchronous
	    final Future<Track> trackFuture = getTrackRequest.executeAsync();

	    // Do other things...

	    // Wait for the request to complete
	    final Track tracks = trackFuture.get();

	    // Print something's name
	    System.out.println("Name: " + tracks.getName());
	  } catch (Exception e) {
	    System.out.println("Something went wrong!\n" + e.getMessage());
	  }
	}
}

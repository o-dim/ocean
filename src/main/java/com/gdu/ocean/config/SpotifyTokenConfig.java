package com.gdu.ocean.config;

import java.io.IOException;

import org.apache.hc.core5.http.ParseException;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.credentials.ClientCredentials;
import se.michaelthelin.spotify.requests.authorization.client_credentials.ClientCredentialsRequest;
import se.michaelthelin.spotify.requests.data.search.simplified.SearchTracksRequest;

@Component
public class SpotifyTokenConfig {
	private static final String CLIENT_ID = "9a7cef176a8642788a4e8562efe66b69";
    private static final String CLIENT_SECRET = "bac7768e97d449e5a078629e3cc469bb";

    private static final SpotifyApi spotifyApi = new SpotifyApi.Builder().setClientId(CLIENT_ID).setClientSecret(CLIENT_SECRET).build();

    public static String accessToken(){
        ClientCredentialsRequest clientCredentialsRequest = spotifyApi.clientCredentials().build();
        try {
            final ClientCredentials clientCredentials = clientCredentialsRequest.execute(); // 흑흑 ㅠ ㅅ ㅠ <- 해결햇으니 울지마요 ... 
            // Set access token for further "spotifyApi" object usage
            spotifyApi.setAccessToken(clientCredentials.getAccessToken());
            return spotifyApi.getAccessToken();
        } catch (IOException | ParseException | SpotifyWebApiException e) {
            System.out.println("Error: " + e.getMessage());
            return "error";
        }
    }
    

}

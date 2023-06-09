package com.gdu.ocean.config;

import java.io.IOException;

import javax.management.Query;
import javax.servlet.http.HttpServletRequest;

import org.apache.hc.core5.http.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import com.neovisionaries.i18n.CountryCode;

import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.credentials.ClientCredentials;
import se.michaelthelin.spotify.requests.authorization.client_credentials.ClientCredentialsRequest;
import se.michaelthelin.spotify.requests.data.search.simplified.SearchTracksRequest;

@Configuration
@Component
public class SpotifyConfig {
	private static final String CLIENT_ID = "9a7cef176a8642788a4e8562efe66b69";
    private static final String CLIENT_SECRET = "bac7768e97d449e5a078629e3cc469bb";

    private static final SpotifyApi spotifyApi = new SpotifyApi.Builder().setClientId(CLIENT_ID).setClientSecret(CLIENT_SECRET).build();

    public static String accessToken(){
        ClientCredentialsRequest clientCredentialsRequest = spotifyApi.clientCredentials().build();
        try {
            final ClientCredentials clientCredentials = clientCredentialsRequest.execute();
            // Set access token for further "spotifyApi" object usage
            spotifyApi.setAccessToken(clientCredentials.getAccessToken());
            return spotifyApi.getAccessToken();
        } catch (IOException | ParseException | SpotifyWebApiException e) {
            System.out.println("Error: " + e.getMessage());
            return "error";
        }
    }

    
    // 노래로 검색
    @Bean
    public SearchTracksRequest searchTracksRequest() {
        return new SearchTracksRequest.Builder(CLIENT_ID)
            .market(CountryCode.KR) // 검색 결과의 국가를 설정할 수 있습니다. (옵션)
            .limit(10) // 반환할 검색 결과의 최대 수를 설정할 수 있습니다. (옵션)
            .offset(0) // 검색 결과의 오프셋을 설정할 수 있습니다. (옵션)
            .includeExternal("audio") // 검색 결과에 외부 음원 링크를 포함할 수 있습니다. (옵션)
            .build();
    }
    
    // 가수로검색
    @Bean
    public SearchTracksRequest searchArtistsRequest() {
        return new SearchTracksRequest.Builder(CLIENT_ID)
            .market(CountryCode.KR) // 검색 결과의 국가를 설정할 수 있습니다. (옵션)
            .limit(10) // 반환할 검색 결과의 최대 수를 설정할 수 있습니다. (옵션)
            .offset(0) // 검색 결과의 오프셋을 설정할 수 있습니다. (옵션)
            .includeExternal("audio") // 검색 결과에 외부 음원 링크를 포함할 수 있습니다. (옵션)
            .build();
    }
    

}

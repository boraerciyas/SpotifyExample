package com.spotifyexample.demo.service;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.SpotifyHttpManager;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import com.wrapper.spotify.model_objects.specification.User;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;

@Service
public class SpotifyService {
    private final static String CLIENTID = "afe7d3ba155d4d209ae09dfb61d2efa0";
    private final static String CLIENTSECRET = "8dafc43c2000415aa96fc7f03adcae85";
    private final static URI REDIRECTURI = SpotifyHttpManager.makeUri("http://localhost:8080/callback");

    private SpotifyApi spotifyApi;

    public SpotifyService() {
        this.spotifyApi = new SpotifyApi.Builder()
                .setClientId(CLIENTID)
                .setClientSecret(CLIENTSECRET)
                .setRedirectUri(REDIRECTURI)
                .build();
    }

    public URI getAuthorizationUri() {
        return this.spotifyApi.authorizationCodeUri()
                .scope("user-read-private user-read-email")
                .show_dialog(true)
                .build()
                .execute();
    }

    public void completeAuthorizationFromCode(String code) throws IOException, SpotifyWebApiException {
        AuthorizationCodeRequest tokenRequest = spotifyApi.authorizationCode(code).build();
        AuthorizationCodeCredentials credentials = tokenRequest.execute();

        spotifyApi.setAccessToken(credentials.getAccessToken());
        spotifyApi.setRefreshToken(credentials.getRefreshToken());
    }

    public User getUserInfo() throws IOException, SpotifyWebApiException {
        return spotifyApi.getCurrentUsersProfile().build().execute();
    }

    public void logout() {
        this.spotifyApi.setAccessToken(null);
        this.spotifyApi.setRefreshToken(null);
    }
}

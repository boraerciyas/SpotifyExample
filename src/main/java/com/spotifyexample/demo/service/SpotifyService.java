package com.spotifyexample.demo.service;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.SpotifyHttpManager;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import com.wrapper.spotify.model_objects.miscellaneous.Device;
import com.wrapper.spotify.model_objects.specification.User;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.TimeInfo;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

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

    private Timer timer = new Timer();
    private TimerTask task  = new TimerTask() {
        @Override
        public void run() {
            try {
                System.out.println("Inside Timer Task : " + getCurrentTime().toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    public String startResumeUsersPlaybackRequest() throws IOException, SpotifyWebApiException {

        Device[] devices = getAvailableDevices();
        Device device = devices[0];
        String deviceId = device.getId();

        System.out.println("DeviceID : " + deviceId + "\nCurrent Time : " + getCurrentTime());

        timer.schedule(task, 10000, 10000);

        System.out.println(getCurrentTime());

        return this.spotifyApi.startResumeUsersPlayback()
                .context_uri("spotify:album:5zT1JLIj9E57p3e1rFm9Uq")
                .device_id(deviceId)
                .build()
                .execute();
    }

    public URI getAuthorizationUri() {
        return this.spotifyApi.authorizationCodeUri()
                .scope("user-read-private user-read-email user-read-playback-state user-modify-playback-state user-read-currently-playing")
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

    public Device[] getAvailableDevices() throws IOException, SpotifyWebApiException {
        return spotifyApi.getUsersAvailableDevices().build().execute();
    }
    public void logout() {
        this.spotifyApi.setAccessToken(null);
        this.spotifyApi.setRefreshToken(null);
    }

    public Date getCurrentTime() throws IOException {
        String TIME_SERVER = "time-a.nist.gov";
        NTPUDPClient timeClient = new NTPUDPClient();
        InetAddress inetAddress = InetAddress.getByName(TIME_SERVER);
        TimeInfo timeInfo = timeClient.getTime(inetAddress);
        long returnTime = timeInfo.getMessage().getTransmitTimeStamp().getTime();
        return new Date(returnTime);
    }
}

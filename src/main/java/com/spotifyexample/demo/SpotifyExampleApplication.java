package com.spotifyexample.demo;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.SpotifyHttpManager;
import com.wrapper.spotify.model_objects.specification.Paging;
import com.wrapper.spotify.model_objects.specification.TrackSimplified;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;
import com.wrapper.spotify.requests.data.albums.GetAlbumsTracksRequest;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@SpringBootApplication
public class SpotifyExampleApplication {

	final static String clientId = "afe7d3ba155d4d209ae09dfb61d2efa0";
	final static String clientSecret = "8dafc43c2000415aa96fc7f03adcae85";
	final static URI redirectUri = SpotifyHttpManager.makeUri("http://localhost:8080/callback");

	final static String arminVanBuurenID = "0SfsnGyD8FpIN4U4WCkBZ5";

	private static SpotifyApi spotifyApi = new SpotifyApi.Builder()
			.setClientId(clientId)
			.setClientSecret(clientSecret)
			.setRedirectUri(redirectUri)
			.build();

	private static final AuthorizationCodeUriRequest auth = spotifyApi.authorizationCodeUri()
			.scope("user-read-private user-read-email")
			.show_dialog(true)
			.build();
	private static void authorizationCodeUri_Sync() {
		final URI uri = auth.execute();
		System.out.println("URI: " + uri.toString());
	}


//	private static final GetAlbumsTracksRequest getAlbumsTracksRequest = spotifyApi.getAlbumsTracks(arminVanBuurenID)
//			.limit(10)
//			.offset(0)
//			.build();
//
//	private static void getAlbumsTracks_Async() {
//		try {
//			final Future<Paging<TrackSimplified>> pagingFuture = getAlbumsTracksRequest.executeAsync();
//
//			// ...
//
//			final Paging<TrackSimplified> trackSimplifiedPaging = pagingFuture.get();
//
//			System.out.println("Total: " + trackSimplifiedPaging.getTotal());
//		} catch (InterruptedException | ExecutionException e) {
//			System.out.println("Error: " + e.getCause().getMessage());
//		}
//	}
	public static void main(String[] args) throws URISyntaxException {
		SpringApplication.run(SpotifyExampleApplication.class, args);

		authorizationCodeUri_Sync();

//
	}
}

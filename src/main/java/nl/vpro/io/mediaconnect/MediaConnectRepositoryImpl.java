package nl.vpro.io.mediaconnect;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

import javax.inject.Inject;
import javax.inject.Named;

import com.google.api.client.auth.oauth2.AuthorizationCodeTokenRequest;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;

import nl.vpro.io.mediaconnect.domain.MCObjectMapper;
import nl.vpro.io.mediaconnect.domain.MCSchedule;


/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
@Slf4j
@NoArgsConstructor
public class MediaConnectRepositoryImpl implements MediaConnectRepository {

    private String api;

    private String clientId;

    private String clientSecret;


    @Getter
    private TokenResponse tokenResponse;
    @Getter
    private Instant expiration;


    @Inject
    @lombok.Builder
    MediaConnectRepositoryImpl(
        @Named("mediaconnect.api") String api,
        @Named("mediaconnect.client_id") String clientId,
        @Named("mediaconnect.client_secret") String clientSecret
    ) {
        this.api = api == null ? "https://api.eu1.graphlr.io/v5/" : api;
        this.clientId = clientId;
        this.clientSecret = clientSecret;

    }


    @Override
    public MCSchedule getSchedule(UUID channel, LocalDate from, LocalDate until) throws IOException {
        NetHttpTransport netHttpTransport = new NetHttpTransport.Builder()
            .build();

        GenericUrl url = new GenericUrl(api + "prepr/schedules/" + channel + "/guide");




        url.set("from", from.toString());
        url.set("until", until.toString());
        //uri.addParameter("environment_id", "45ed5691-8bc1-4018-9d67-242150cff944");
        url.set("fields", "timelines,show{tags,cover{source_file}},users");

        HttpRequest httpRequest = netHttpTransport.createRequestFactory()
            .buildGetRequest(url);


        authenticate(httpRequest);

        com.google.api.client.http.HttpResponse execute = httpRequest.execute();


        return MCObjectMapper.INSTANCE.readerFor(MCSchedule.class).readValue(execute.getContent());

    }


    protected void authenticate() throws  IOException {

        if (tokenResponse == null || expiration.isBefore(Instant.now())) {
            log.info("Authenticating {}@{}", clientId, api);
            tokenResponse =
                new AuthorizationCodeTokenRequest(new NetHttpTransport(), new JacksonFactory(),
                    new GenericUrl(api + "oauth/access_token"), "authorization_code")
                    //.setRedirectUri("https://localhost")
                    .set("client_id", clientId)
                    .set("client_secret", clientSecret)
                    .setGrantType("client_credentials")
                    .set("scope", "webhooks,tags,taggroups,publications,prepr,containers,assets")
                    .execute();
            expiration = Instant.now().plusSeconds(tokenResponse.getExpiresInSeconds());
        }

    }


    protected void authenticate(HttpRequest request) throws IOException {
        authenticate();
        request.getHeaders().setAuthorization(tokenResponse.getTokenType() + " " + tokenResponse.getAccessToken());


    }
}

package nl.vpro.io.mediaconnect;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;
import com.google.api.client.auth.oauth2.AuthorizationCodeTokenRequest;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.http.GenericUrl;
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
    public MCSchedule getSchedule(UUID channel, LocalDate from, LocalDate until) throws IOException, URISyntaxException {
        URIBuilder uri = new URIBuilder(api + "prepr/schedules/" + channel + "/guide");;
        uri.addParameter("from", from.toString());
        uri.addParameter("until", until.toString());
        //uri.addParameter("environment_id", "45ed5691-8bc1-4018-9d67-242150cff944");
        uri.addParameter("fields", "timelines,show{tags,cover{source_file}},users");

        HttpGet get = new HttpGet(uri.build());

        authenticate(get);

        HttpClient client = HttpClients.createDefault();

        HttpResponse execute = client.execute(get);


        return MCObjectMapper.INSTANCE.readerFor(MCSchedule.class).readValue(execute.getEntity().getContent());

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


    protected void authenticate(HttpRequestBase request) throws IOException {
        authenticate();
        request.addHeader("Authorization", tokenResponse.getTokenType() + " " + tokenResponse.getAccessToken());


    }
}

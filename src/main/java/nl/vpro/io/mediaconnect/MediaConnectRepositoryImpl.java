package nl.vpro.io.mediaconnect;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.MediaType;

import com.google.api.client.auth.oauth2.AuthorizationCodeTokenRequest;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;

import nl.vpro.io.mediaconnect.domain.MCItems;
import nl.vpro.io.mediaconnect.domain.MCObjectMapper;
import nl.vpro.io.mediaconnect.domain.MCSchedule;
import nl.vpro.io.mediaconnect.domain.MCWebhook;


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
        @Named("mediaconnect.clientId") String clientId,
        @Named("mediaconnect.clientSecret") String clientSecret
    ) {
        this.api = api == null ? "https://api.eu1.graphlr.io/v5/" : api;
        this.clientId = clientId;
        this.clientSecret = clientSecret;

    }


    @Override
    public MCSchedule getSchedule(UUID channel, LocalDate from, LocalDate until) throws IOException {
        GenericUrl url = createUrl("prepr", "schedules", channel,  "guide");
        url.set("from", from.toString());
        url.set("until", until.toString());
        //uri.addParameter("environment_id", "45ed5691-8bc1-4018-9d67-242150cff944");
        url.set("fields", "timelines,show{tags,cover{source_file}},users");

        return get(url, MCSchedule.class);
    }


    @Override
    public MCItems<MCWebhook> getWebhooks() throws IOException {
        GenericUrl url = createUrl("webhooks");
        return get(url, MCItems.class);
    }

    @Override
    public void createWebhook(MCWebhook webhook) throws IOException {
        GenericUrl url = createUrl("webhooks", UUID.randomUUID().toString());
        HttpResponse response = put(url, webhook);
        log.info("{} ", response);

    }

    @Override
    public void deleteWebhook(UUID webhook) throws IOException {
        GenericUrl url = createUrl("webhooks", UUID.randomUUID().toString());

    }

    GenericUrl createUrl(Object ... path) {
        GenericUrl url = new GenericUrl(api);
        boolean append = false;
        for (Object p : path) {
            if (append) {
                url.appendRawPath("/");
            }
            url.appendRawPath(p.toString());
            append = true;
        }
        return url;
    }

    @Override
    public String toString() {
        return clientId + "@" + api;
    }


    <T> T get(GenericUrl url, Class<T> clazz) throws IOException {
        HttpResponse execute = get(url);

        return MCObjectMapper.INSTANCE.readerFor(clazz).readValue(execute.getContent());
    }


    protected HttpResponse get(GenericUrl url) throws IOException {
        NetHttpTransport netHttpTransport = new NetHttpTransport.Builder()
            .build();

        HttpRequest httpRequest = netHttpTransport.createRequestFactory()
            .buildGetRequest(url);


        authenticate(httpRequest);

        return execute(httpRequest);

    }

      protected HttpResponse put(GenericUrl url, Object o) throws IOException {
          NetHttpTransport netHttpTransport = new NetHttpTransport.Builder()
              .build();

          ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
          MCObjectMapper.INSTANCE.writeValue(outputStream, o);
          log.info("Putting {}", new String(outputStream.toByteArray()));
          HttpRequest httpRequest = netHttpTransport.createRequestFactory()
              .buildPutRequest(url, new ByteArrayContent(MediaType.APPLICATION_JSON, outputStream.toByteArray()));

          authenticate(httpRequest);


          return execute(httpRequest);


    }

    protected HttpResponse execute(HttpRequest httpRequest) throws IOException {
         log.info("Calling {} {}", httpRequest.getRequestMethod(), httpRequest.getUrl());
        HttpResponse execute = httpRequest.execute();


          return execute;
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

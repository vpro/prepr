package nl.vpro.io.mediaconnect;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.MediaType;

import com.google.api.client.auth.oauth2.AuthorizationCodeTokenRequest;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;

import nl.vpro.io.mediaconnect.domain.MCItems;
import nl.vpro.io.mediaconnect.domain.MCObjectMapper;
import nl.vpro.io.mediaconnect.domain.MCSchedule;
import nl.vpro.io.mediaconnect.domain.MCWebhook;


/**
 * Provides the actual implementation of {@link MediaConnectRepository}. This is implemented by being a rest client, so it has to be configured
 * with credentials.
 *
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

    private final NetHttpTransport netHttpTransport = new NetHttpTransport.Builder()
          .build();



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
    public MCWebhook createWebhook(String callback_url, List<String> events) throws IOException {
        GenericUrl url = createUrl("webhooks");
        Map<String, Object> post = new HashMap<>();
        post.put("callback_url", callback_url);
        post.put("events", events);

        HttpResponse response = post(url, post);
        return MCObjectMapper.INSTANCE.readerFor(MCWebhook.class).readValue(response.getContent());
    }

    @Override
    public void deleteWebhook(UUID webhook) throws IOException {
        GenericUrl url = createUrl("webhooks", webhook);
        delete(url);

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
        HttpRequest httpRequest = netHttpTransport.createRequestFactory()
            .buildGetRequest(url);

        return execute(httpRequest);

    }


    protected HttpResponse delete(GenericUrl url) throws IOException {
        HttpRequest httpRequest = netHttpTransport.createRequestFactory()
            .buildDeleteRequest(url);

        return execute(httpRequest);

    }

    protected HttpResponse put(GenericUrl url, Object o) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        MCObjectMapper.INSTANCE.writeValue(outputStream, o);
        log.info("Putting {}", new String(outputStream.toByteArray()));
        return execute(netHttpTransport.createRequestFactory()
            .buildPutRequest(url, new ByteArrayContent(MediaType.APPLICATION_JSON, outputStream.toByteArray())));
    }


    @SuppressWarnings("unchecked")
    protected HttpResponse post(GenericUrl url, Map<String, Object> form) throws IOException {
        log.debug("Posting {}", form);
        Map<String, String> map = new TreeMap<>();
        form.forEach((k, v) -> {
            if (v instanceof Collection) {
                AtomicInteger i = new AtomicInteger(0);
                ((Collection) v).forEach((e) -> {
                    map.put(k + "[" + i.getAndIncrement() + "]", toString(e));
                });
            } else {
                map.put(k, toString(v));
            }
        });

        HttpRequest httpRequest = netHttpTransport.createRequestFactory()
            .buildPostRequest(url, new UrlEncodedContent(map));
        return execute(httpRequest);
    }

    protected String toString(Object o) {
            return o.toString();
        }

    protected HttpResponse execute(HttpRequest httpRequest) throws IOException {
        log.info("Calling {} {}", httpRequest.getRequestMethod(), httpRequest.getUrl());
        authenticate(httpRequest);

        return httpRequest.execute();
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
                    .set("scope", "webhooks,tags,taggroups,publications,prepr,containers,assets,webhooks_publish,webhooks_delete")
                    .execute();
            expiration = Instant.now().plusSeconds(tokenResponse.getExpiresInSeconds());
            log.info("Token -> {}", tokenResponse.getAccessToken());
        }

    }


    protected void authenticate(HttpRequest request) throws IOException {
        authenticate();
        request.getHeaders().setAuthorization(tokenResponse.getTokenType() + " " + tokenResponse.getAccessToken());


    }
}

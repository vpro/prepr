package nl.vpro.io.mediaconnect;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.MediaType;

import com.google.api.client.auth.oauth2.AuthorizationCodeTokenRequest;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;

import nl.vpro.io.mediaconnect.domain.*;


/**
 * Provides the actual implementation of {@link MediaConnectRepository}. This is implemented by being a rest client, so it has to be configured
 * with credentials.
 *
 * This can be done by code (using {@link MediaConnectRepositoryImpl#builder()}, using config file {@link MediaConnectRepositoryImpl#configuredInUserHome()}
 * or using some IoC-framework (depending on the {@link Inject} and {@link Named} annotations on the constructor.
 *
 * @author Michiel Meeuwissen
 * @since 0.1
 */
@Slf4j
public class MediaConnectRepositoryImpl implements MediaConnectRepository {

    private static String RATELIMIT_RESET = "X-Graphlr-RateLimit-Reset";
    private static String RATELIMIT_HOURREMAINING = "X-Graphlr-RateLimit-Hour-Remaining";
    private static String RATELIMIT_HOURLIMIT = "X-Graphlr-RateLimit-Hour-Limit";

    private static final NetHttpTransport NET_HTTP_TRANSPORT = new NetHttpTransport.Builder()
        .build();

    private final String api;

    private final String clientId;

    private final String clientSecret;

    @Getter
    private Integer rateLimitReset = null;
    @Getter
    private Integer rateLimitHourRemaining = null;
    @Getter
    private Integer rateLimitHourLimit = null;

    @Setter
    @Getter
    private boolean logAsCurl;

    @Getter
    private TokenResponse tokenResponse;

    @Getter
    private Instant expiration;

    @Getter
    private final MediaConnectTimelines timelines = new MediaConnectTimelinesImpl(this);

    @Getter
    private final MediaConnectWebhooks webhooks = new MediaConnectWebhooksImpl(this);

    @Getter
    private final MediaConnectAssets assets = new MediaConnectAssetsImpl(this);


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

    public static MediaConnectRepositoryImpl configuredInUserHome() {

        try {
            Properties properties = new Properties();
            properties.load(new FileInputStream(System.getProperty("user.home") + File.separator + "conf" + File.separator + "mediaconnect.properties"));
            return MediaConnectRepositoryImpl
                .builder()
                .api(properties.getProperty("mediaconnect.api"))
                .clientId(properties.getProperty("mediaconnect.clientId"))
                .clientSecret(properties.getProperty("mediaconnect.clientSecret"))
                .build();
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }




    @Override
    public MCItems<?> getChannels(Paging paging) {
        GenericUrl url = createUrl("channels");
        addListParameters(url, paging);
        return get(url, MCItems.class);


    }

    @Override
    public MCItems<?> getPublicationsForChannel(
        Paging paging, @Nonnull  UUID channel, LocalDateTime event_from, LocalDateTime event_until) {
        GenericUrl url = createUrl("publications");
        addListParameters(url, paging);
        url.set("channel_id", channel);
        //url.set("label", "Post");
        if (event_from != null) {
            url.set("event_from", event_from);
        }
        if (event_until != null) {
            url.set("event_until", event_until);
        }
        return get(url, MCItems.class);
    }



    @Override
    public  <T extends MCContent> T getPublication(
        @Nonnull  UUID id) {
        GenericUrl url = createUrl("publications", id.toString());
        //url.set("label", "Post");
        url.set("status", "published");
        url.set("fields", "container,tags,element{media{source_file{resized{picture.width(1920)}}}}");
        return (T) get(url, MCContent.class);
    }





    protected void addListParameters(GenericUrl url, Paging paging) {
        if (paging.getSkip() != null) {
            url.set("skip", paging.getSkip());
        }
        if (paging.getLimit() != null) {
            url.set("limit", paging.getLimit());
        }
        if (paging.getAfter() != null) {
            log.warn("Not tested");
        }
           if (paging.getBefore() != null) {
            log.warn("Not tested");
        }

    }


    @Override
    public String toString() {
        return clientId + "@" + api;
    }


    @SneakyThrows(IOException.class)
    protected <T> T get(GenericUrl url, Class<T> clazz) {
        HttpResponse execute = get(url);
        return MCObjectMapper.INSTANCE.readerFor(clazz)
            .readValue(execute.getContent());
    }

    protected void consumerGraphrlHeader(HttpResponse response) {
        rateLimitReset  = Integer.parseInt(response.getHeaders().getFirstHeaderStringValue(RATELIMIT_RESET));
        rateLimitHourRemaining  = Integer.parseInt(response.getHeaders().getFirstHeaderStringValue(RATELIMIT_HOURREMAINING));
        rateLimitHourLimit  = Integer.parseInt(response.getHeaders().getFirstHeaderStringValue(RATELIMIT_HOURLIMIT));
    }


    protected HttpResponse get(GenericUrl url) throws IOException {
        return execute(NET_HTTP_TRANSPORT.createRequestFactory()
            .buildGetRequest(url));
    }

    protected HttpResponse delete(GenericUrl url) throws IOException {
        return execute(NET_HTTP_TRANSPORT.createRequestFactory()
            .buildDeleteRequest(url));
    }

    protected HttpResponse put(GenericUrl url, Object o) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        MCObjectMapper.INSTANCE.writeValue(outputStream, o);
        return execute(NET_HTTP_TRANSPORT.createRequestFactory()
            .buildPutRequest(url, new ByteArrayContent(MediaType.APPLICATION_JSON, outputStream.toByteArray())));
    }


    @SuppressWarnings("unchecked")
    protected HttpResponse post(GenericUrl url, Map<String, Object> form) throws IOException {
        log.debug("Posting {}", form);
        Map<String, String> map = new TreeMap<>();
        form.forEach((k, v) -> {
            if (v.getClass().isArray()) {
                v = Arrays.asList((Object[]) v);
            }
            if (v instanceof Collection) {
                AtomicInteger i = new AtomicInteger(0);
                ((Collection) v).forEach((e) ->
                    map.put(k + "[" + i.getAndIncrement() + "]", toString(e))
                );
            } else {
                map.put(k, toString(v));
            }
        });

        HttpRequest httpRequest = NET_HTTP_TRANSPORT.createRequestFactory()
            .buildPostRequest(url, new UrlEncodedContent(map));
        return execute(httpRequest);
    }

    protected String toString(Object o) {
        return o == null ? "" : String.valueOf(o);
    }

    protected HttpResponse execute(HttpRequest httpRequest) throws IOException {
        authenticate(httpRequest);
        if (logAsCurl) {
            log.info("Calling \ncurl -X{} -H 'Authorization: {} {}' '{}'\n", httpRequest.getRequestMethod(), tokenResponse.getTokenType(), tokenResponse.getAccessToken(), httpRequest.getUrl());
        } else {
            log.info("Calling {} {}", httpRequest.getRequestMethod(), httpRequest.getUrl());
        }
        HttpResponse response = httpRequest.execute();
        consumerGraphrlHeader(response);
        return response;
    }


    protected void authenticate(HttpRequest request) throws IOException {
        getToken();
        request.getHeaders().setAuthorization(tokenResponse.getTokenType() + " " + tokenResponse.getAccessToken());
    }
    protected void getToken() throws  IOException {
        if (tokenResponse == null || expiration.isBefore(Instant.now())) {
            log.debug("Authenticating {}@{}", clientId, api);
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
            log.info("Authenticated {}@{} -> Token  {}", clientId, api, tokenResponse.getAccessToken());
        }

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

}

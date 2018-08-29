package nl.vpro.io.mediaconnect;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Named;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.ws.rs.core.MediaType;

import com.google.api.client.auth.oauth2.AuthorizationCodeTokenRequest;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;

import nl.vpro.io.mediaconnect.domain.MCObjectMapper;


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
public class MediaConnectRepositoryImpl implements MediaConnectRepository, MediaConnectRepositoryImplMXBean {

    private static String RATELIMIT_RESET         = "X-Graphlr-RateLimit-Reset";
    private static String RATELIMIT_HOURREMAINING = "X-Graphlr-RateLimit-Hour-Remaining";
    private static String RATELIMIT_HOURLIMIT     = "X-Graphlr-RateLimit-Hour-Limit";

    static String SOURCEFILE_FIELD = "source_file{resized{picture.width(1920)}}";

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
    private final MediaConnectPrepr prepr = new MediaConnectPreprImpl(this);


    @Getter
    private final MediaConnectGuides guides = new MediaConnectGuidesImpl(this);

    @Getter
    private final MediaConnectWebhooks webhooks = new MediaConnectWebhooksImpl(this);

    @Getter
    private final MediaConnectAssets assets = new MediaConnectAssetsImpl(this);

    @Getter
    private final MediaConnectContent content = new MediaConnectContentImpl(this);

    @Getter
    private final MediaConnectTags tags = new MediaConnectTagsImpl(this);

    @lombok.Builder(builderClassName = "Builder")
    MediaConnectRepositoryImpl(
        String api,
        @Nonnull String clientId,
        @Nonnull String clientSecret
    ) {
        this.api = api == null ? "https://api.eu1.graphlr.io/v5/" : api;
        this.clientId = clientId;
        this.clientSecret = clientSecret;


    }

    public void registerBean(String jmxName) {
        String name = jmxName == null || jmxName.length() == 0  ? "mediaconnectRepository"  : jmxName;

        try {
            MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
            ObjectName objectName = new ObjectName("nl.vpro.io.mediaconnect:name=" + name + "-" + clientId);
            if (! mbs.isRegistered(objectName)) {
                mbs.registerMBean(this, objectName);
                log.info("Registered {}", objectName);
            } else {
                log.info("Already registered {}", objectName);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @SuppressWarnings("unchecked")
    @SneakyThrows(IOException.class)
    public static MediaConnectRepositoryImpl configuredInUserHome(String channel) {

        Properties properties = new Properties();
        properties.load(new FileInputStream(System.getProperty("user.home") + File.separator + "conf" + File.separator + "mediaconnect.properties"));
        return configured((Map) properties, channel);
    }

    public static MediaConnectRepositoryImpl configured(Map<String, String> properties, String channel) {
        String postfix = channel == null || channel.length() == 0 ? "" : "." + channel;
        MediaConnectRepositoryImpl impl = MediaConnectRepositoryImpl
            .builder()
            .api(properties.get("mediaconnect.api"))
            .clientId(properties.get("mediaconnect.clientId" + postfix))
            .clientSecret(properties.get("mediaconnect.clientSecret" + postfix))
            .build();
        impl.setLogAsCurl(Boolean.parseBoolean(properties.get("mediaconnect.logascurl")));
        String jmxName = properties.get("mediaconnect.jmxname");
        if (jmxName != null && jmxName.length() > 0) {
            impl.registerBean(jmxName);
        }

        return impl;
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

    protected void consumeGraphrlHeaders(HttpResponse response) {
        rateLimitReset  = Integer.parseInt(response.getHeaders().getFirstHeaderStringValue(RATELIMIT_RESET));
        rateLimitHourRemaining  = Integer.parseInt(response.getHeaders().getFirstHeaderStringValue(RATELIMIT_HOURREMAINING));
        rateLimitHourLimit  = Integer.parseInt(response.getHeaders().getFirstHeaderStringValue(RATELIMIT_HOURLIMIT));
    }


    @SneakyThrows(IOException.class)
    protected HttpResponse get(GenericUrl url)  {
        return execute(NET_HTTP_TRANSPORT.createRequestFactory()
            .buildGetRequest(url));
    }

    @SneakyThrows(IOException.class)
    protected HttpResponse delete(GenericUrl url) {
        return execute(NET_HTTP_TRANSPORT.createRequestFactory()
            .buildDeleteRequest(url));
    }

    @SneakyThrows(IOException.class)
    protected HttpResponse put(GenericUrl url, Object o)  {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        MCObjectMapper.INSTANCE.writeValue(outputStream, o);
        return execute(NET_HTTP_TRANSPORT.createRequestFactory()
            .buildPutRequest(url, new ByteArrayContent(MediaType.APPLICATION_JSON, outputStream.toByteArray())));
    }


    @SuppressWarnings("unchecked")
    @SneakyThrows(IOException.class)
    protected HttpResponse post(GenericUrl url, Map<String, Object> form) {
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

    @SneakyThrows(IOException.class)
    protected <T> T post(GenericUrl url, Map<String, Object> map, Class<T> clazz) {
        HttpResponse execute = post(url, map);
        return MCObjectMapper.INSTANCE.readerFor(clazz)
            .readValue(execute.getContent());
    }

    protected String toString(Object o) {
        return o == null ? "" : String.valueOf(o);
    }

    @SneakyThrows(IOException.class)
    protected HttpResponse execute(HttpRequest httpRequest)  {
        authenticate(httpRequest);
        if (logAsCurl) {
            HttpContent content = httpRequest.getContent();
            String data = "";
            if (content != null) {
                UrlEncodedContent urlEncoded = (UrlEncodedContent) content;
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                urlEncoded.writeTo(out);
                data = " -d '" + out.toString() + "'";
            }
            log.info("Calling \ncurl -X{} -H 'Authorization: {} {}' '{}' {}\n", httpRequest.getRequestMethod(), tokenResponse.getTokenType(), tokenResponse.getAccessToken(), httpRequest.getUrl(), data);
        } else {
            log.info("Calling {} {}", httpRequest.getRequestMethod(), httpRequest.getUrl());
        }
        HttpResponse response = httpRequest.execute();
        consumeGraphrlHeaders(response);
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
                    .set("scope", "webhooks,tags,taggroups,publications,prepr,containers,assets,webhooks_publish,webhooks_delete,guides")
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

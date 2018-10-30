package nl.vpro.io.mediaconnect;

import lombok.Getter;
import lombok.Setter;
import lombok.Singular;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.reflect.Constructor;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.StringUtils;

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
 * This can be done by code (using {@link MediaConnectRepositoryImpl#builder()}, using config file {@link MediaConnectRepositoryImpl#configuredInUserHome(String)}}
 * or using some IoC-framework (depending on the {@link Inject} and {@link Named} annotations on the constructor.
 *
 * @author Michiel Meeuwissen
 * @since 0.1
 */
@Slf4j
@Named
public class MediaConnectRepositoryImpl implements MediaConnectRepository, MediaConnectRepositoryImplMXBean {

    private static String RATELIMIT_RESET         = "X-Graphlr-RateLimit-Reset";
    private static String RATELIMIT_HOURREMAINING = "X-Graphlr-RateLimit-Hour-Remaining";
    private static String RATELIMIT_HOURLIMIT     = "X-Graphlr-RateLimit-Hour-Limit";

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
    @Setter
    private UUID guideId;


    @Getter
    @Setter
    private String channel;

    @Getter
    private TokenResponse tokenResponse;

    @Getter
    private Instant expiration;

    @Inject
    private Provider<MediaConnectPrepr> prepr = createAndMemoize(MediaConnectPreprImpl.class);


    @Inject
    private Provider<MediaConnectGuides> guides = createAndMemoize(MediaConnectGuidesImpl.class);

    @Inject
    private Provider<MediaConnectWebhooks> webhooks = createAndMemoize(MediaConnectWebhooksImpl.class);

    @Inject
    private Provider<MediaConnectAssets> assets =  createAndMemoize(MediaConnectAssetsImpl.class);

    @Inject
    private Provider<MediaConnectContent> content = createAndMemoize(MediaConnectContentImpl.class);

    @Inject
    private Provider<MediaConnectTags> tags = createAndMemoize(MediaConnectTagsImpl.class);

    @Inject
    private Provider<MediaConnectContainers> containers = createAndMemoize(MediaConnectContainersImpl.class);

    private Creator creator = new CreatorImpl();

    @Getter
    private List<Scope> scopes;

    @lombok.Builder(builderClassName = "Builder")
    MediaConnectRepositoryImpl(
        @Nullable String api,
        @Nonnull String channel,
        @Nonnull String clientId,
        @Nonnull String clientSecret,
        @Nullable String guideId,
        @Nullable Creator creator,
        @Nullable @Singular  List<Scope> scopes,
        boolean logAsCurl
    ) {
        this.api = api == null ? "https://api.eu1.graphlr.io/v5/" : api;
        this.channel = channel;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.guideId = guideId == null ? null : UUID.fromString(guideId);
        this.scopes = scopes;
        this.logAsCurl = logAsCurl;
        this.creator = creator == null ? this.creator : creator;
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

    public static MediaConnectRepositoryImpl configured(
        Map<String, String> properties,
        String channel) {
        String postfix = channel == null || channel.length() == 0 ? "" : "." + channel;
        String clientId = properties.get("mediaconnect.clientId" + postfix);
        if (StringUtils.isNotBlank(clientId)) {
            boolean logAsCurl = Boolean.valueOf(properties.getOrDefault("mediaconnect.logascurl", "false"));
            MediaConnectRepositoryImpl impl = MediaConnectRepositoryImpl
                .builder()
                .channel(channel)
                .api(properties.get("mediaconnect.api"))
                .clientId(clientId)
                .clientSecret(properties.get("mediaconnect.clientSecret" + postfix))
                .guideId(properties.get("mediaconnect.guideId" + postfix))
                .scopesAsString(properties.get("mediaconnect.scopes" + postfix))
                .logAsCurl(logAsCurl)
                .build();
            String jmxName = properties.get("mediaconnect.jmxname");
            if (jmxName != null && jmxName.length() > 0) {
                impl.registerBean(jmxName);
            }
            String scopes = properties.get("mediaconnect.scopes");
            if (scopes != null && scopes.length() > 0) {
                if (impl.getScopes() == null || impl.getScopes().isEmpty()) {
                    impl.setScopes(Arrays.stream(scopes.split("\\s*,\\s*")).map(Scope::valueOf).collect(Collectors.toList()));
                }
            }


            return impl;
        } else {
            throw new IllegalArgumentException("No client id found for " + channel + " in " + properties.keySet());
        }
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
        return channel + "=" + clientId + "@" + api;
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
            List<Scope> scopesToUse = scopes;
            if (scopesToUse == null || scopesToUse.isEmpty()) {
                scopesToUse = Arrays.asList(Scope.values());
                log.info("No scopes configured, using {}", scopesToUse);

            }
            log.debug("Authenticating {}@{} with scopes {}", clientId, api, scopesToUse);
            if (StringUtils.isBlank(clientSecret)) {
                throw new IllegalStateException("No client secret defined for " + clientId);
            }
            tokenResponse =
                new AuthorizationCodeTokenRequest(new NetHttpTransport(), new JacksonFactory(),
                    new GenericUrl(api + "oauth/access_token"), "authorization_code")
                    //.setRedirectUri("https://localhost")
                    .set("client_id", clientId)
                    .set("client_secret", clientSecret)
                    .setGrantType("client_credentials")
                    .set("scope", scopesToUse.stream().map(Enum::name).collect(Collectors.joining(",")))
                    .execute();
            expiration = Instant.now().plusSeconds(tokenResponse.getExpiresInSeconds());
            log.info("Authenticated {}@{} -> Token  {}", clientId, api, tokenResponse.getAccessToken());
        }

    }

    public void setScopes(List<Scope> scopes) {
        this.scopes = scopes;
        tokenResponse = null;
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
    public MediaConnectPrepr getPrepr() {
        return prepr.get();

    }

    @Override
    public MediaConnectGuides getGuides() {
        return guides.get();

    }

    @Override
    public MediaConnectWebhooks getWebhooks() {
        return webhooks.get();

    }

    @Override
    public MediaConnectAssets getAssets() {
        return assets.get();

    }

    @Override
    public MediaConnectContent getContent() {
        return content.get();

    }

    @Override
    public MediaConnectTags getTags() {
        return tags.get();

    }

    @Override
    public MediaConnectContainers getContainers() {
        return containers.get();

    }

    public static class Builder {
        Builder scopesAsString(String scopes) {
            if (StringUtils.isNotEmpty(scopes)) {
                return scopes(Arrays.stream(scopes.split("\\s*,\\s*")).map(Scope::valueOf).collect(Collectors.toList()));
            }
            return this;
        }
    }

    @FunctionalInterface
    public interface Creator {
        <T> T apply(Class<T> objectClass);
    }

    public class CreatorImpl implements Creator {

        @Override
        @SneakyThrows
        public <T> T apply(Class<T> objectClass) {
            Constructor<T> constructor = objectClass.getConstructor(MediaConnectRepositoryImpl.class);
            return constructor.newInstance(MediaConnectRepositoryImpl.this);
        }
    }

    protected <T, S extends T> Provider<T> createAndMemoize(Class<S> clazz) {
        return memoize(() -> creator.apply(clazz));
    }

    public static <T> Provider<T> memoize(Provider<T> provider) {

        return new Provider<T>() {
            T memoized;
            @Override
            public T get() {
                if (memoized == null) {
                    memoized = provider.get();
                    log.info("Created without IOC {}", memoized);
                }
                return memoized;

            }
        };
    }

}

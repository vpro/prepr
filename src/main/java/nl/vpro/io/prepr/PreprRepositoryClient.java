package nl.vpro.io.prepr;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Named;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.api.client.auth.oauth2.AuthorizationCodeTokenRequest;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;

import nl.vpro.io.prepr.domain.PreprObjectMapper;


/**

 *
 * @author Michiel Meeuwissen
 * @since 0.1
 */
@Named
public class PreprRepositoryClient implements PreprRepositoryClientMXBean {

    private static String RATELIMIT_RESET         = "X-Graphlr-RateLimit-Reset";
    private static String RATELIMIT_HOURREMAINING = "X-Graphlr-RateLimit-Hour-Remaining";
    private static String RATELIMIT_HOURLIMIT     = "X-Graphlr-RateLimit-Hour-Limit";

    private static final NetHttpTransport NET_HTTP_TRANSPORT = new NetHttpTransport.Builder()
        .build();

    private final HttpRequestInitializer getInitializer;

    private final Logger log;

    private final String api;

    private final String clientId;

    private final String clientSecret;

    @Getter
    private Integer rateLimitReset = null;
    @Getter
    private Integer rateLimitHourRemaining = null;
    @Getter
    private Integer rateLimitHourLimit = null;

    @Getter
    private Integer authenticationCount = 0;

    @Getter
    private Integer callCount = 0;

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

    @Getter
    private List<Scope> scopes;

    @Getter
    private String  description;

    @Getter
    private Duration connectTimeoutForGet = Duration.ofMinutes(2);

    @Getter
    private Duration readTimeoutForGet = Duration.ofMinutes(2);


    @lombok.Builder(builderClassName = "Builder")
    PreprRepositoryClient(
        // Look out with adding parameters.  This method is also used in nl.vpro.io.mediaconnect.spring.AbstractSpringMediaConnectRepositoriesConfiguration.postProcessBeanDefinitionRegistry
        @Nullable @Named("prepr.api") String api,
        @Nonnull String channel,
        @Nonnull String clientId,
        @Nonnull String clientSecret,
        @Nullable String guideId,
        @Nullable @Named("prepr.scopes") String scopes,
        @Nullable String description,
        @Named("prepr.logascurl") Boolean logAsCurl) {
        this.api = api == null ? "https://api.eu1.graphlr.io/v5/" : api;
        this.channel = channel;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.guideId = guideId == null ? null : UUID.fromString(guideId);
        this.scopes = scopes == null ? Arrays.asList() : Arrays.stream(scopes.split("\\s*,\\s*")).map(Scope::valueOf).collect(Collectors.toList());
        if (logAsCurl != null) {
            this.logAsCurl = logAsCurl;
        }
        this.log = LoggerFactory.getLogger(PreprRepositoryImpl.class.getName() + "." + channel);
        this.description = description;
        this.getInitializer  = request -> {
            request.setConnectTimeout((int) connectTimeoutForGet.toMillis());
            request.setReadTimeout((int) readTimeoutForGet.toMillis());
        };
    }

    public void registerBean(String jmxName) {
        String name = jmxName == null || jmxName.length() == 0  ? "preprRepository"  : jmxName;

        try {
            MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
            ObjectName objectName = new ObjectName("nl.vpro.io.prepr:name=" + name + "-" + clientId);
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


    protected <T> T _get(GenericUrl url, Class<T> clazz) throws IOException {
        HttpResponse execute = get(url);
        return PreprObjectMapper.INSTANCE.readerFor(clazz)
            .readValue(execute.getContent());
    }


    @SneakyThrows(IOException.class)
    protected <T> Optional<T> optionalGet(GenericUrl url, Class<T> clazz)  {
        try {
            T result = _get(url, clazz);
            return Optional.of(result);
        } catch (HttpResponseException re) {
            if (re.getStatusCode() == 404) {
                return Optional.empty();
            }
            throw re;
        } catch(RuntimeException e) {
            throw new RuntimeException("For " + url.toURI() + ":" + e.getMessage(), e);
        }
    }
    protected <T> T get(GenericUrl url, Class<T> clazz)  {
        return optionalGet(url, clazz).orElse(null);
    }


    protected void consumeGraphrlHeaders(HttpResponse response) {
        rateLimitReset  = Integer.parseInt(response.getHeaders().getFirstHeaderStringValue(RATELIMIT_RESET));
        rateLimitHourRemaining  = Integer.parseInt(response.getHeaders().getFirstHeaderStringValue(RATELIMIT_HOURREMAINING));
        rateLimitHourLimit  = Integer.parseInt(response.getHeaders().getFirstHeaderStringValue(RATELIMIT_HOURLIMIT));
    }


    protected HttpResponse get(GenericUrl url) throws IOException {
        return execute(NET_HTTP_TRANSPORT.createRequestFactory(getInitializer)
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
        PreprObjectMapper.INSTANCE.writeValue(outputStream, o);
        return execute(NET_HTTP_TRANSPORT.createRequestFactory()
            .buildPutRequest(url, new ByteArrayContent(MediaType.APPLICATION_JSON, outputStream.toByteArray())));
    }


    @SuppressWarnings("unchecked")
    @SneakyThrows(IOException.class)
    protected HttpResponse post(GenericUrl url, Map<String, Object> form) {
        log.debug("{}: Posting {}", channel, form);
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
        return PreprObjectMapper.INSTANCE.readerFor(clazz)
            .readValue(execute.getContent());
    }

    protected String toString(Object o) {
        return o == null ? "" : String.valueOf(o);
    }

    protected HttpResponse execute(HttpRequest httpRequest) throws IOException {
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
        callCount++;
        request.getHeaders().setAuthorization(tokenResponse.getTokenType() + " " + tokenResponse.getAccessToken());
    }

    protected synchronized  void getToken() throws  IOException {
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
            log.debug("Authenticated {}@{} -> Token  {}", clientId, api, tokenResponse.getAccessToken());
            authenticationCount++;
        }
    }

    public void setScopes(List<Scope> scopes) {
        this.scopes = scopes;
        tokenResponse = null;
    }


    @Override
    public String getScopesAsString() {
        return String.valueOf(this.scopes.toString());
    }

    @Override
    public String getConnectTimeoutForGetAsString() {
        return connectTimeoutForGet.toString();
    }

    @Override
    public void setConnectTimeoutForGetAsString(String connectTimeoutForGetAsString) {
        this.connectTimeoutForGet = Duration.parse(connectTimeoutForGetAsString);

    }

    @Override
    public String getReadTimeoutForGetAsString() {
        return readTimeoutForGet.toString();

    }

    @Override
    public void setReadTimeoutForGetAsString(String readTimeoutForGetAsString) {
        this.readTimeoutForGet = Duration.parse(readTimeoutForGetAsString);

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

    public static class Builder {
        Builder scope(Scope... scopes) {
            return scopes(Arrays.stream(scopes).map(Enum::name).collect(Collectors.joining(",")));
        }
    }

}

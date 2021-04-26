package nl.vpro.io.prepr;

import lombok.*;

import java.io.*;
import java.lang.management.ManagementFactory;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import javax.inject.Named;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.validation.constraints.Size;
import javax.ws.rs.core.MediaType;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.api.client.auth.oauth2.AuthorizationCodeTokenRequest;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;

import nl.vpro.io.prepr.domain.PreprObjectMapper;


/**

 *
 * @author Michiel Meeuwissen
 * @since 0.1
 */
@Named
public class PreprRepositoryClient implements PreprRepositoryClientMXBean {


    public enum Version {
        v5("v5/"),
        v6("");
        @Getter
        private final String path;

        Version(String path) {
            this.path = path;
        }
    }

    private static final String RATELIMIT_RESET         = "X-Graphlr-RateLimit-Reset";
    private static final String RATELIMIT_HOURREMAINING = "X-Graphlr-RateLimit-Hour-Remaining";
    private static final String RATELIMIT_HOURLIMIT     = "X-Graphlr-RateLimit-Hour-Limit";

    private static final NetHttpTransport NET_HTTP_TRANSPORT = new NetHttpTransport.Builder()
        .build();

    private final HttpRequestInitializer getInitializer;

    private final Logger log;

    private final String api;

    private final String clientId;

    private final String clientSecret;

    private final String clientToken;


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
    private final String  description;

    @Getter
    private Duration connectTimeoutForGet = Duration.ofMinutes(2);

    @Getter
    private Duration readTimeoutForGet = Duration.ofMinutes(2);

    @Getter
    private Duration mininumExpiration = Duration.ofSeconds(20);

    @Getter
    @Setter
    @Size(min = 1)
    private int guideCallsMaxDays = 1;

    @Getter
    @Setter
    private Duration delayAfterToken = Duration.ofMillis(200);

    @Getter
    @Setter
    private Version version;


    @lombok.Builder(builderClassName = "Builder")
    PreprRepositoryClient(
        // Look out with adding parameters.  This method is also used in nl.vpro.io.prepr.spring.AbstractSpringPreprRepositoriesConfiguration.postProcessBeanDefinitionRegistry
        @Nullable @Named("prepr.api") String api,
        @NonNull String channel,
        @NonNull String clientId,
        @Nullable String clientSecret,
        @Nullable String clientToken,
        @Nullable String guideId,
        @Nullable @Named("prepr.scopes") String scopes,
        @Nullable String description,
        @Named("prepr.logascurl") Boolean logAsCurl,
        @Nullable Integer guideCallsMaxDays,
        @Named("prepr.delayAfterToken") Duration delayAfterToken,
        Version version

        ) {
        this.log = LoggerFactory.getLogger(PreprRepositoryImpl.class.getName() + "." + channel);
        this.api = getApiUrl(api, this.log);
        this.version = version == null ? Version.v5 : version;
        this.channel = channel;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.clientToken = clientToken;
        if (this.clientSecret == null && this.clientToken == null) {
            throw new IllegalArgumentException("Either a client token, or a client secret must be configured for " + clientId + "@" + channel);
        }
        this.guideId = guideId == null ? null : UUID.fromString(guideId);
        this.scopes = scopes == null ? Collections.emptyList() : Arrays.stream(scopes.split("\\s*,\\s*")).map(Scope::valueOf).collect(Collectors.toList());
        if (logAsCurl != null) {
            this.logAsCurl = logAsCurl;
        }
        this.description = description;
        this.getInitializer  = request -> {
            request.setConnectTimeout((int) connectTimeoutForGet.toMillis());
            request.setReadTimeout((int) readTimeoutForGet.toMillis());
        };
        if (guideCallsMaxDays != null) {
            this.guideCallsMaxDays = guideCallsMaxDays;
        }
        if (delayAfterToken != null) {
            this.delayAfterToken = delayAfterToken;
        }
    }

    private static String getApiUrl(String setting, Logger log) {
        if (setting == null) {
            return "https://api.eu1.graphlr.io/";
        } else {
            if (setting.endsWith("/v5/")) {
                log.warn("Api base url seems to contains the version ({}). This should not happen any more. Stripping it of now.", setting);
                setting = setting.substring(0, setting.length() - 3);
            }
            return setting;
        }
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
        return channel + "=" + clientId + "@" + getBaseUrl();
    }


    protected <T> T _get(GenericUrl url, Class<T> clazz) throws IOException {
        HttpResponse execute = get(url);
        try {
            InputStream content = execute.getContent();
            return PreprObjectMapper.INSTANCE.readerFor(clazz)
                .readValue(content);
        } finally {
            execute.disconnect();
        }
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
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                if (content instanceof UrlEncodedContent) {
                    UrlEncodedContent urlEncoded = (UrlEncodedContent) content;
                    urlEncoded.writeTo(out);
                } else if (content instanceof ByteArrayContent) {
                    IOUtils.copy(((ByteArrayContent) content).getInputStream(), out);
                } else {
                    log.warn("Didn't recognized {}", content);
                }
                data = " -d '" + out.toString() + "'";
            }
            if (lifetimeToken()) {
                log.info("Calling \ncurl -X{} -H 'Authorization: {} {}' '{}' {}\n", httpRequest.getRequestMethod(), "Bearer", clientToken, httpRequest.getUrl(), data);
            } else {
                log.info("Calling \ncurl -X{} -H 'Authorization: {} {}' '{}' {}\n", httpRequest.getRequestMethod(), tokenResponse.getTokenType(), tokenResponse.getAccessToken(), httpRequest.getUrl(), data);
            }
        } else {
            log.info("Calling {} {}", httpRequest.getRequestMethod(), httpRequest.getUrl());
        }
        HttpResponse response = httpRequest.execute();
        consumeGraphrlHeaders(response);
        return response;
    }


    protected void authenticate(HttpRequest request) throws IOException {
        callCount++;
        if (lifetimeToken()) {
            request.getHeaders().setAuthorization("Bearer " + clientToken);
        } else {
            getToken();
            request.getHeaders().setAuthorization(tokenResponse.getTokenType() + " " +  tokenResponse.getAccessToken());

        }
    }

    protected boolean lifetimeToken() {
        return StringUtils.isNotBlank(clientToken) && StringUtils.isBlank(clientSecret);
    }

    protected synchronized  void getToken() throws  IOException {
        if (lifetimeToken()) {
            return;
        }
        if (tokenResponse == null || expiration.isBefore(Instant.now().plus(mininumExpiration))) {

            List<Scope> scopesToUse = scopes;
            if (scopesToUse == null || scopesToUse.isEmpty()) {
                scopesToUse = Arrays.asList(Scope.values());
                log.info("No scopes configured, using {}", scopesToUse);

            }
            log.debug("Authenticating {}@{} with scopes {}", clientId, getBaseUrl(), scopesToUse);
            if (StringUtils.isBlank(clientSecret)) {
                throw new IllegalStateException("No client secret defined for " + clientId);
            }
            boolean refresh = tokenResponse != null;
            tokenResponse =
                new AuthorizationCodeTokenRequest(new NetHttpTransport(), GsonFactory.getDefaultInstance(),
                    new GenericUrl(getBaseUrl() + "oauth/access_token"), "authorization_code")
                    //.setRedirectUri("https://localhost")
                    .set("client_id", clientId)
                    .set("client_secret", clientSecret)
                    .setGrantType("client_credentials")
                    .set("scope", scopesToUse.stream().map(Enum::name).collect(Collectors.joining(",")))
                    .execute();
            expiration = Instant.now().plusSeconds(tokenResponse.getExpiresInSeconds());
            Instant refreshToken = expiration.minus(mininumExpiration);
            Duration duration = Duration.between(Instant.now(), refreshToken);
            String prefix = refresh ? "Refreshed authentication" : "Authenticated";
            if (duration.isNegative()) {
                log.info("{} {}@{} -> Token  {} (will be refreshed at {} - {} = {}, which is immediately!)", prefix, clientId, getBaseUrl(), tokenResponse.getAccessToken(), expiration, mininumExpiration, refreshToken);
            } else {
                log.info("{} {}@{} -> Token  {} (will be refreshed at {} - {} = {}, i.e. after {})", prefix, clientId, getBaseUrl(), tokenResponse.getAccessToken(), expiration, mininumExpiration, refreshToken, duration);
            }
            authenticationCount++;
            long delayInMillis = delayAfterToken.toMillis();
            if (delayInMillis > 0) {
                try {
                    log.info("Delaying {} after token to avoid occasional 401 with token", delayAfterToken);
                    Thread.sleep(delayInMillis);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    log.error(e.getMessage(), e);
                }
            }
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
    public String getExpirationAsString() {
        return expiration.toString();


    }

    @Override
    public String getRefreshesAfterAsString() {
        return expiration.minus(mininumExpiration).toString();

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

    @Override
    public String getDelayAfterTokenAsString() {
        return delayAfterToken.toString();
    }

    @Override
    public void setDelayAfterTokenAsString(String string) {
        this.delayAfterToken = Duration.parse(string);
    }


    GenericUrl createUrl(Object ... path) {
        GenericUrl url = new GenericUrl(getBaseUrl());
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

    protected String getBaseUrl() {
        return api + version.getPath();
    }

    public static class Builder {
        Builder scope(Scope... scopes) {
            return scopes(Arrays.stream(scopes).map(Enum::name).collect(Collectors.joining(",")));
        }
    }

}

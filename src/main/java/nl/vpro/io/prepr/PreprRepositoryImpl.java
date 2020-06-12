package nl.vpro.io.prepr;

import lombok.Getter;
import lombok.SneakyThrows;

import java.io.*;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.nullness.qual.NonNull;

import static nl.vpro.io.prepr.PreprRepositoryClient.Version.v5;

/**
 * Provides the actual implementation of {@link PreprRepository}. This is implemented by being a rest client, so it has to be configured
 *  with credentials.
 *  *
 *  This can be done by code (using {@link PreprRepositoryClient#builder()}, using config file {@link PreprRepositoryImpl#configuredInUserHome(String)}}
 *  or using some IoC-framework (depending on the {@link Inject} and {@link Named} annotations on the constructor.
 * @author Michiel Meeuwissen
 * @since 0.3
 */
@Getter
@Named
public class PreprRepositoryImpl implements PreprRepository {
    private final PreprPrepr prepr;

    private final PreprGuides guides;

    private final PreprWebhooks webhooks;

    private final PreprAssets assets;

    private final PreprContent content;

    private final PreprTags tags;

    private final PreprContainers containers;

    private final PreprPersons persons;

    @Getter
    private final PreprRepositoryClient v5Client;

    @Getter
    private final PreprRepositoryClient v6Client;

    @Getter
    private final URI baseUrl;


    @Inject
    public PreprRepositoryImpl(
        PreprRepositoryClient v5Client,
        PreprRepositoryClient v6Client,
        PreprPrepr prepr,
        PreprGuides guides,
        PreprWebhooks webhooks,
        PreprAssets assets,
        PreprContent content,
        PreprTags tags,
        PreprContainers containers,
        PreprPersons persons,
        URI baseUrl
        ) {
        this.prepr = prepr;
        this.guides = guides;
        this.webhooks = webhooks;
        this.assets = assets;
        this.content = content;
        this.tags = tags;
        this.containers = containers;
        this.persons = persons;
        this.v5Client = v5Client;
        this.v6Client = v6Client;
        this.baseUrl = baseUrl;
    }

    public PreprRepositoryImpl(
        PreprRepositoryClient v5Client,
        PreprRepositoryClient v6Client,
        URI baseUrl
        ) {
        this.prepr = new PreprPreprImpl(v5Client);
        this.guides = new PreprGuidesImpl(v5Client);
        this.webhooks = new PreprWebhooksImpl(v5Client);
        this.assets = new PreprAssetsImpl(v5Client);
        this.content = new PreprContentImpl(v5Client);
        this.tags = new PreprTagsImpl(v5Client);
        this.containers = new PreprContainersImpl(v5Client);
        this.persons = new PreprPersonsImpl(v5Client);
        this.v5Client = v5Client;
        this.v6Client = v6Client;
        this.baseUrl = baseUrl;
    }


    @SuppressWarnings("unchecked")
    @SneakyThrows(IOException.class)
    public static PreprRepositoryImpl configuredInUserHome(String channel) {

        Properties properties = new Properties();
        properties.load(new FileInputStream(System.getProperty("user.home") + File.separator + "conf" + File.separator + "prepr.properties"));
        return configured((Map) properties, channel);
    }

    public static PreprRepositoryImpl configured(
        Map<String, String> properties,
        @NonNull String channel) {
        String postfix = channel == null || channel.length() == 0 ? "" : "." + channel;
        String clientId = properties.get("prepr.clientId" + postfix);
        if (StringUtils.isNotBlank(clientId)) {
            PreprRepositoryClient v5Client = createClient(v5, properties, channel);
            PreprRepositoryClient v6Client = null; // not yet used
            String baseUrl = properties.get("prepr.baseUrl" + postfix);

            return new PreprRepositoryImpl(
                v5Client,
                v6Client,
                new PreprPreprImpl(v5Client),
                new PreprGuidesImpl(v5Client),
                new PreprWebhooksImpl(v5Client),
                new PreprAssetsImpl(v5Client),
                new PreprContentImpl(v5Client),
                new PreprTagsImpl(v5Client),
                new PreprContainersImpl(v5Client),
                new PreprPersonsImpl(v5Client),
                baseUrl == null ? null : URI.create(baseUrl)
            );
        } else {
            throw new IllegalArgumentException("No client id found for " + channel + " in " + properties.keySet());
        }
    }

    private static PreprRepositoryClient createClient(
        PreprRepositoryClient.Version version,
        Map<String, String> properties,
        @NonNull String channel) {
        String postfix = channel == null || channel.length() == 0 ? "" : "." + channel;
        String clientId = properties.get("prepr.clientId" + postfix);
        boolean logAsCurl = Boolean.parseBoolean(properties.getOrDefault("prepr.logascurl", "false"));
        PreprRepositoryClient client = PreprRepositoryClient.builder()
                .channel(channel)
                .api(properties.get("prepr.api"))
                .clientId(clientId)
                .clientSecret(properties.get("prepr.clientSecret" + postfix))
                .guideId(properties.get("prepr.guideId" + postfix))
                .scopes(properties.get("prepr.scopes" + postfix))
                .description(properties.get("prepr.description" + postfix))
                .logAsCurl(logAsCurl)
                .version(version)
                .build();
         String jmxName = properties.get("prepr.jmxname");
        if (jmxName != null && jmxName.length() > 0) {
            client.registerBean(jmxName);
        }
        String scopes = properties.get("prepr.scopes");
        if (StringUtils.isNotBlank(scopes)) {
            if (CollectionUtils.isEmpty(client.getScopes())) {
                client.setScopes(Arrays.stream(scopes.split("\\s*,\\s*")).map(Scope::valueOf).collect(Collectors.toList()));
            }
        }
        return client;
    }

    @Override
    public String getChannel() {
        return getClient().getChannel();
    }

    @Override
    public String toString() {
        return getClient().toString();
    }
}

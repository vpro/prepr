package nl.vpro.io.prepr;

import lombok.Getter;
import lombok.SneakyThrows;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

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
    private final PreprRepositoryClient client;


    @Inject
    public PreprRepositoryImpl(
        PreprRepositoryClient client,
        PreprPrepr prepr,
        PreprGuides guides,
        PreprWebhooks webhooks,
        PreprAssets assets,
        PreprContent content,
        PreprTags tags,
        PreprContainers containers,
        PreprPersons persons
        ) {
        this.prepr = prepr;
        this.guides = guides;
        this.webhooks = webhooks;
        this.assets = assets;
        this.content = content;
        this.tags = tags;
        this.containers = containers;
        this.persons = persons;
        this.client = client;
    }

    public PreprRepositoryImpl(
        PreprRepositoryClient client
        ) {
        this.prepr = new PreprPreprImpl(client);
        this.guides = new PreprGuidesImpl(client);
        this.webhooks = new PreprWebhooksImpl(client);
        this.assets = new PreprAssetsImpl(client);
        this.content = new PreprContentImpl(client);
        this.tags = new PreprTagsImpl(client);
        this.containers = new PreprContainersImpl(client);
        this.persons = new PreprPersonsImpl(client);
        this.client = client;
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
        String channel) {
        String postfix = channel == null || channel.length() == 0 ? "" : "." + channel;
        String clientId = properties.get("prepr.clientId" + postfix);
        if (StringUtils.isNotBlank(clientId)) {
            boolean logAsCurl = Boolean.valueOf(properties.getOrDefault("prepr.logascurl", "false"));
            PreprRepositoryClient client = PreprRepositoryClient
                .builder()
                .channel(channel)
                .api(properties.get("prepr.api"))
                .clientId(clientId)
                .clientSecret(properties.get("prepr.clientSecret" + postfix))
                .guideId(properties.get("prepr.guideId" + postfix))
                .scopes(properties.get("prepr.scopes" + postfix))
                .description(properties.get("prepr.description" + postfix))
                .logAsCurl(logAsCurl)
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

            return new PreprRepositoryImpl(client,
                new PreprPreprImpl(client),
                new PreprGuidesImpl(client),
                new PreprWebhooksImpl(client),
                new PreprAssetsImpl(client),
                new PreprContentImpl(client),
                new PreprTagsImpl(client),
                new PreprContainersImpl(client),
                new PreprPersonsImpl(client)
            );
        } else {
            throw new IllegalArgumentException("No client id found for " + channel + " in " + properties.keySet());
        }
    }

    @Override
    public String getChannel() {
        return client.getChannel();
    }

    @Override
    public String toString() {
        return client.toString();
    }
}

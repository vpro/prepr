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
 * Provides the actual implementation of {@link MediaConnectRepository}. This is implemented by being a rest client, so it has to be configured
 *  with credentials.
 *  *
 *  This can be done by code (using {@link MediaConnectRepositoryClient#builder()}, using config file {@link MediaConnectRepositoryImpl#configuredInUserHome(String)}}
 *  or using some IoC-framework (depending on the {@link Inject} and {@link Named} annotations on the constructor.
 * @author Michiel Meeuwissen
 * @since 0.3
 */
@Getter
@Named
public class MediaConnectRepositoryImpl implements MediaConnectRepository {
    private final MediaConnectPrepr prepr;

    private final MediaConnectGuides guides;

    private final MediaConnectWebhooks webhooks;

    private final MediaConnectAssets assets;

    private final MediaConnectContent content;

    private final MediaConnectTags tags;

    private final MediaConnectContainers containers;

    private final MediaConnectPersons persons;

    @Getter
    private final MediaConnectRepositoryClient client;


    @Inject
    public MediaConnectRepositoryImpl(
        MediaConnectRepositoryClient client,
        MediaConnectPrepr prepr,
        MediaConnectGuides guides,
        MediaConnectWebhooks webhooks,
        MediaConnectAssets assets,
        MediaConnectContent content,
        MediaConnectTags tags,
        MediaConnectContainers containers,
        MediaConnectPersons persons
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

    public MediaConnectRepositoryImpl(
        MediaConnectRepositoryClient client
        ) {
        this.prepr = new MediaConnectPreprImpl(client);
        this.guides = new MediaConnectGuidesImpl(client);
        this.webhooks = new MediaConnectWebhooksImpl(client);
        this.assets = new MediaConnectAssetsImpl(client);
        this.content = new MediaConnectContentImpl(client);
        this.tags = new MediaConnectTagsImpl(client);
        this.containers = new MediaConnectContainersImpl(client);
        this.persons = new MediaConnectPersonsImpl(client);
        this.client = client;
    }


    @SuppressWarnings("unchecked")
    @SneakyThrows(IOException.class)
    public static MediaConnectRepositoryImpl configuredInUserHome(String channel) {

        Properties properties = new Properties();
        properties.load(new FileInputStream(System.getProperty("user.home") + File.separator + "conf" + File.separator + "prepr.properties"));
        return configured((Map) properties, channel);
    }

    public static MediaConnectRepositoryImpl configured(
        Map<String, String> properties,
        String channel) {
        String postfix = channel == null || channel.length() == 0 ? "" : "." + channel;
        String clientId = properties.get("prepr.clientId" + postfix);
        if (StringUtils.isNotBlank(clientId)) {
            boolean logAsCurl = Boolean.valueOf(properties.getOrDefault("prepr.logascurl", "false"));
            MediaConnectRepositoryClient client = MediaConnectRepositoryClient
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

            return new MediaConnectRepositoryImpl(client,
                new MediaConnectPreprImpl(client),
                new MediaConnectGuidesImpl(client),
                new MediaConnectWebhooksImpl(client),
                new MediaConnectAssetsImpl(client),
                new MediaConnectContentImpl(client),
                new MediaConnectTagsImpl(client),
                new MediaConnectContainersImpl(client),
                new MediaConnectPersonsImpl(client)
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

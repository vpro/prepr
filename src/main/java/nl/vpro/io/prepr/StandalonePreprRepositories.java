package nl.vpro.io.prepr;

import lombok.Getter;
import lombok.Singular;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.StringUtils;

import nl.vpro.io.prepr.domain.MCObjectMapper;

/**
 * Maintains a map of {@link PreprRepository}. A MediaConnectRepository connects to precisely one channel. If you need to sync with more than one, this may come in handy.
 *
 * @author Michiel Meeuwissen
 * @since 0.3
 */
@Slf4j
public class StandalonePreprRepositories implements PreprRepositories {

    @Getter
    private final Map<String, PreprRepository> repositories = new TreeMap<>();

    @lombok.Builder(builderClassName = "Builder")
    public StandalonePreprRepositories(
        @Singular Map<String, PreprRepository> repositories) {
        this.repositories.putAll(repositories);
    }


    /**
     * <p>Given a map of properties, created a an instant of {@link StandalonePreprRepositories}.
     *</p>
     * <p>
     * It will use properties like
     * </p>
     * <code>{@literal
     *     prepr.clientId.<CHANNEL>
     * }</code>
     * <p>
     * to determin which {@link PreprRepository}s most be instantiated and configured with other properties like:
     * </p>
     *  <code>{@literal
     *          mediaconnect.<property>.<CHANNEL>
     * }</code>
     */
    public static StandalonePreprRepositories fromMap(Map<String, String> configuration) {
        Builder builder = StandalonePreprRepositories.builder();
        String prefix = "prepr.clientId.";
        List<String> channels = configuration.entrySet().stream()
            .filter((e) -> StringUtils.isNotBlank(e.getValue()))
            .map(Map.Entry::getKey)
            .filter((k) -> k.startsWith(prefix))
            .map((k) -> k.substring(prefix.length()))
            .collect(Collectors.toList());
        for (String channel : channels) {
            String secret = configuration.get("prepr.clientSecret." + channel);
            if (StringUtils.isNotEmpty(secret)) {
                builder.repository(channel,
                    PreprRepositoryImpl
                        .configured(configuration, channel)
                );
            } else {
                log.info("Ignored prepr repository for {}  because no client secret configured", channel);
            }
        }
        String lenient = configuration.get("prepr.lenientjson");
        MCObjectMapper.configureInstance(StringUtils.isBlank(lenient) || Boolean.parseBoolean(lenient));
        return builder.build();
    }

    @Override
    @Nonnull
    public Iterator<PreprRepository> iterator() {
        return repositories.values().iterator();
    }

    @Override
    public Optional<PreprRepository> get(String channel) {
        return Optional.ofNullable(repositories.get(channel));
    }
    @Override
    public String toString() {
        return repositories.toString();
    }
}

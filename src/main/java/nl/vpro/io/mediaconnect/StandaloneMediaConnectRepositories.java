package nl.vpro.io.mediaconnect;

import lombok.Getter;
import lombok.Singular;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.StringUtils;

import nl.vpro.io.mediaconnect.domain.MCObjectMapper;

/**
 * Maintains a map of {@link MediaConnectRepository}. A MediaConnectRepository connects to precisely one channel. If you need to sync with more than one, this may come in handy.
 *
 * @author Michiel Meeuwissen
 * @since 0.3
 */
@Slf4j
public class StandaloneMediaConnectRepositories implements MediaConnectRepositories {

    @Getter
    private final Map<String, MediaConnectRepository> repositories = new TreeMap<>();

    @lombok.Builder(builderClassName = "Builder")
    public StandaloneMediaConnectRepositories(
        @Singular Map<String, MediaConnectRepository> repositories) {
        this.repositories.putAll(repositories);
    }

    public static StandaloneMediaConnectRepositories fromMap(Map<String, String> configuration) {
        Builder builder = StandaloneMediaConnectRepositories.builder();
        String prefix = "mediaconnect.clientId.";
        List<String> channels = configuration.entrySet().stream()
            .filter((e) -> StringUtils.isNotBlank(e.getValue()))
            .map(Map.Entry::getKey)
            .filter((k) -> k.startsWith(prefix))
            .map((k) -> k.substring(prefix.length()))
            .collect(Collectors.toList());
        for (String channel : channels) {
            String secret = configuration.get("mediaconnect.clientSecret." + channel);
            if (StringUtils.isNotEmpty(secret)) {
                builder.repository(channel,
                    MediaConnectRepositoryImpl
                        .configured(configuration, channel)
                );
            } else {
                log.info("Ignored mediaconnect repository for {}  because no client secret configured", channel);
            }
        }
        String lenient = configuration.get("mediaconnect.lenientjson");
        MCObjectMapper.configureInstance(StringUtils.isBlank(lenient) || Boolean.parseBoolean(lenient));
        return builder.build();
    }

    @Override
    @Nonnull
    public Iterator<MediaConnectRepository> iterator() {
        return repositories.values().iterator();
    }

    @Override
    public Optional<MediaConnectRepository> get(String channel) {
        return Optional.ofNullable(repositories.get(channel));
    }
    @Override
    public String toString() {
        return repositories.toString();
    }
}
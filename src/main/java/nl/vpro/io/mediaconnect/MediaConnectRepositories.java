package nl.vpro.io.mediaconnect;

import lombok.Getter;
import lombok.Singular;

import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import nl.vpro.io.mediaconnect.domain.MCObjectMapper;

/**
 * Maintains a map of {@link MediaConnectRepository}. A MediaConnectRepository connects to precisely one channel. If you need to sync with more than one, this may come in handy.
 *
 * @author Michiel Meeuwissen
 * @since 0.3
 */
public class MediaConnectRepositories implements Iterable<MediaConnectRepository> {

    @Getter
    private final Map<String, MediaConnectRepository> repositories = new TreeMap<>();

    @lombok.Builder
    public MediaConnectRepositories(
        @Singular Map<String, MediaConnectRepository> repositories) {
        this.repositories.putAll(repositories);
    }

    public static MediaConnectRepositories fromMap(Map<String, String> configuration) {
        MediaConnectRepositoriesBuilder builder = MediaConnectRepositories.builder();
        String prefix = "mediaconnect.clientId.";
        List<String> channels = configuration.keySet().stream()
            .filter((k) -> k.startsWith(prefix))
            .map((k) -> k.substring(prefix.length()))
            .collect(Collectors.toList());
        for (String channel : channels) {
            builder.repository(channel,
                MediaConnectRepositoryImpl
                    .configured(configuration, channel)
            );
        }
        String lenient = configuration.get("mediaconnect.lenientjson");
        MCObjectMapper.configureInstance(StringUtils.isBlank(lenient) || Boolean.parseBoolean(lenient));
        return builder.build();
    }

    @Override
    public Iterator<MediaConnectRepository> iterator() {
        return repositories.values().iterator();
    }

    public Optional<MediaConnectRepository> get(String channel) {
        return Optional.ofNullable(repositories.get(channel));
    }
    @Override
    public String toString() {
        return repositories.toString();
    }
}

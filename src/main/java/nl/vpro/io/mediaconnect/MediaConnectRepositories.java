package nl.vpro.io.mediaconnect;

import java.util.Map;
import java.util.Optional;

/**
 * @author Michiel Meeuwissen
 * @since 0.3
 */
public interface MediaConnectRepositories extends Iterable<MediaConnectRepository>  {

    Optional<MediaConnectRepository> get(String channel);

    Map<String, MediaConnectRepository> getRepositories();

}

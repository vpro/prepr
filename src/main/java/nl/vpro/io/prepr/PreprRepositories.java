package nl.vpro.io.prepr;

import java.util.Map;
import java.util.Optional;

/**
 * @author Michiel Meeuwissen
 * @since 0.3
 */
public interface PreprRepositories extends Iterable<PreprRepository>  {

    Optional<PreprRepository> get(String channel);

    Map<String, PreprRepository> getRepositories();

}

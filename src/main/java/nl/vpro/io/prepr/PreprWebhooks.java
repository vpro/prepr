package nl.vpro.io.prepr;

import java.util.UUID;

import nl.vpro.io.prepr.domain.PreprItems;
import nl.vpro.io.prepr.domain.PreprWebhook;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
public interface PreprWebhooks {

    PreprItems<PreprWebhook> get(Paging paging);

    default PreprItems<PreprWebhook> get() {
        return get(Paging.builder().build());
    }

    PreprWebhook create(String url, String... events);

    void delete(UUID webhook);
}

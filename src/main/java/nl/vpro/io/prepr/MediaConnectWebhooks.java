package nl.vpro.io.prepr;

import java.util.UUID;

import nl.vpro.io.prepr.domain.MCItems;
import nl.vpro.io.prepr.domain.MCWebhook;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
public interface MediaConnectWebhooks {

    MCItems<MCWebhook> get(Paging paging);

    default MCItems<MCWebhook> get() {
        return get(Paging.builder().build());
    }

    MCWebhook create(String url, String... events);

    void delete(UUID webhook);
}

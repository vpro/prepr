package nl.vpro.io.mediaconnect;

import java.util.UUID;

import nl.vpro.io.mediaconnect.domain.MCItems;
import nl.vpro.io.mediaconnect.domain.MCWebhook;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
public interface MediaConnectWebhooks {

    MCItems<MCWebhook> getWebhooks(Paging paging);

    default MCItems<MCWebhook> getWebhooks() {
        return getWebhooks(Paging.builder().build());
    }

    MCWebhook createWebhook(String url, String... events);

    void deleteWebhook(UUID webhook);
}

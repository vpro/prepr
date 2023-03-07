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

    /**
     *  {"code":405,"message":"The PUT method is not supported for this route. Supported methods: GET, HEAD, DELETE.","dbtrace":"PRP.1678189824.9643"}
     *
     * @deprecated Seems to be broken. Just delete and recreate?
     */
    @Deprecated
    PreprWebhook put(PreprWebhook wh);

    void delete(UUID webhook);
}
